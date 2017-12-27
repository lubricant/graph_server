package com.soga.social.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.soga.social.data.GraphDB;
import com.soga.social.data.SessionDB;
import com.soga.social.data.SessionDB.Session;
import com.soga.social.data.model.ConnEdge;
import com.soga.social.data.model.PersonNode;
import com.soga.social.data.model.TraverPath;
import com.soga.social.service.SocialGraphServiceGrpc.SocialGraphServiceImplBase;
import com.soga.social.service.data.Properties;

import io.grpc.stub.StreamObserver;

public class SocialGraphServiceImp extends SocialGraphServiceImplBase 
implements Closeable {
    
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final GraphDB graphDB;
	private final SessionDB sessDB;
	
	public SocialGraphServiceImp() throws Exception {
		graphDB = new GraphDB();
		sessDB = new SessionDB();
	}
	
	public void close() throws IOException {
		graphDB.close();
		sessDB.close();
	}
	
	private Result done(boolean success, Message message) {
		Result.Builder builder = Result.newBuilder().setState(success ? Result.Status.SUCCESS: Result.Status.IGNORED);
		if (success && message != null)
			builder.setData(Any.pack(message));
		return builder.build();
	}
	
	private Result fail(Throwable error) {
		Result.Builder builder = Result.newBuilder().setState(Result.Status.FAILURE);
		if (error != null)
			builder.setHint(error.getMessage());
		return builder.build();
	}
	
	public void createPerson(PersonKey request, StreamObserver<Result> response) {
		try {
			int code = graphDB.createPerson(PersonNode.of(request.getId()));
			response.onNext(done(code == 1, null));
		} catch (Exception ex) {
			logger.error("Create person fail.", ex);
			response.onNext(fail(ex));
		} finally {
			response.onCompleted();
		}
    }
	
	public void removePerson(PersonKey request, StreamObserver<Result> response) {
		try {
			PersonNode node = PersonNode.of(request.getId());
			List<ConnEdge> result = graphDB.removePerson(node);
			if (result == null) {
				response.onNext(done(false, null));
			} else {
				Person person = Person.newBuilder().
						setId(node.getPid()).
						putAllProps(Properties.parse(node.getProps()).getProps()).
						build();
				
				List<Connection> connections = result.stream().map(conn->{
					return Connection.newBuilder().
							setSrc(conn.getSrc()).
							setDst(conn.getDst()).
							putAllProps(Properties.parse(conn.getProps()).getProps()).
							build();
				}).collect(Collectors.toList());
				
				response.onNext(done(true, 
						PersonConn.newBuilder().setPerson(person).addAllConnections(connections).build()));
			}
			
		} catch (Exception ex) {
			logger.error("Remove person fail.", ex);
			response.onNext(fail(ex));
		} finally {
			response.onCompleted();
		}
	}

    public void connectPerson(ConnectionKey request, StreamObserver<Result> response) {
		try {
			int code = graphDB.createConnection(
					ConnEdge.of(request.getSrc(), request.getDst()));
			if (code == -1)
				response.onNext(fail(new Exception("At least one person is not found.")));
			else
				response.onNext(done(code == 1, null));
		} catch (Exception ex) {
			logger.error("Connect person fail.", ex);
			response.onNext(fail(ex));
		} finally {
			response.onCompleted();
		}
    }

    public void disconnectPerson(ConnectionKey request, StreamObserver<Result> response) {
		try {
			
			ConnEdge conn = ConnEdge.of(request.getSrc(), request.getDst());
			List<PersonNode> result = graphDB.removeConnection(conn);
			
			if (result == null) {
				response.onNext(done(false, null));
			} else {
				
				Connection connection = Connection.newBuilder().
					setSrc(conn.getSrc()).
					setDst(conn.getDst()).
					putAllProps(Properties.parse(conn.getProps()).getProps()).
					build();
				
				List<Person> persons = result.stream().map(person->{
					return Person.newBuilder().
							setId(person.getPid()).
							putAllProps(Properties.parse(person.getProps()).getProps()).
							build();
				}).collect(Collectors.toList());
				
				response.onNext(done(true, 
						ConnPerson.newBuilder().setConnection(connection).addAllPersons(persons).build()));
			}
			
		} catch (Exception ex) {
			logger.error("Disconnect person fail.", ex);
			response.onNext(fail(ex));
		} finally {
			response.onCompleted();
		}
    }
    
    public void updatePerson(Person request, StreamObserver<Result> response) {
		try {
			boolean success = graphDB.updatePerson(PersonNode.of(request.getId(), 
					Properties.wrap(request.getPropsMap()).getAllProps()));
			response.onNext(done(success, null));
		} catch (Exception ex) {
			logger.error("Update person fail.", ex);
			response.onNext(fail(ex));
		} finally {
			response.onCompleted();
		}
    }
    
	public void updateConnection(Connection request, StreamObserver<Result> response) {
		try {
			boolean success = graphDB.updateConnection(ConnEdge.of(
					request.getSrc(), request.getDst(), 
					Properties.wrap(request.getPropsMap()).getAllProps()));
			response.onNext(done(success, null));
		} catch (Exception ex) {
			logger.error("Update connection fail.", ex);
			response.onNext(fail(ex));
		} finally {
			response.onCompleted();
		}
	}
	
    public void traverseGraph(TraversalDesc request, StreamObserver<Result> response) {
    	try {
    		
	    	if (request.getOneshot()) {
	    		TraverPath path = graphDB.traverse(request.getRoot(), request.getConnected(), request.getDepth());
	    		TraversalTree tree = path == null ? null : TraversalTree.newBuilder().setRoot(parseTraversal(path)).setSize(path.getSize()).build();
	    		response.onNext(done(path != null, tree));
	    	} else {
	    		long ticket = request.getTicket();
	    		if (ticket < 0) ticket = SessionDB.acquireTicket();
	    		
	    		Session sess = sessDB.restoreSess(ticket);
	    		TraverPath path = graphDB.traverse(request.getRoot(), request.getConnected(), request.getDepth(), sess);
	    		TraversalTree tree = path == null ? null : TraversalTree.newBuilder().setRoot(parseTraversal(path)).setSize(path.getSize()).setTicket(ticket).build();
	    		response.onNext(done(path != null, tree));
	    		sessDB.storeSess(ticket, sess);
	    	}
	    	
		} catch (Exception ex) {
			logger.error("TraverseGraph fail.", ex);
			response.onNext(fail(ex));
		} finally {
			response.onCompleted();
		}
    }
    
    private TraversalNode parseTraversal(TraverPath path) {
    	
    	TraversalNode.Builder traver = TraversalNode.newBuilder();
    	
    	traver.setPerson(Person.newBuilder().
    			setId(path.getNode().getPid()).
    			putAllProps(Properties.parse(path.getNode().getProps()).getProps()).
    			build());
    	
    	if (path.getEdge() != null) {
    		traver.setConnection(Connection.newBuilder().
    				setSrc(path.getEdge().getSrc()).
    				setDst(path.getEdge().getDst()).
    				putAllProps(Properties.parse(path.getEdge().getProps()).getProps()).
    				build());
    	}
    	
    	if (! path.getBranches().isEmpty()) {
    		for (TraverPath branch: path.getBranches().values()) {
    			traver.addAdjoin(parseTraversal(branch));
    		}
    	}
    	
    	return traver.build();
    }
    
}
