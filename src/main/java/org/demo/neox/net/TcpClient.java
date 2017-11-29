package org.demo.neox.net;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import io.netty.util.internal.PlatformDependent;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import org.demo.neox.rpc.Message;
import org.demo.neox.rpc.Protocol;

public class TcpClient {
	
	private final AtomicLong sequenceHolder = new AtomicLong(0);
	private final ConcurrentMap<Long, TcpRequest> requestHolder = PlatformDependent.newConcurrentHashMap();
	
	private final Logger logger;
	private final Vertx vertx;
	private NetClient client;
	private NetSocket socket;

	TcpClient(String ip, int port, Vertx vertx, Consumer<TcpClient> cleaner) throws Exception {
		this.vertx = vertx;
		this.logger = LoggerFactory.getLogger(String.format("TcpClient-[%s:%d]", ip, port));
		if (! connect(ip, port))
			throw new IllegalStateException(String.format("Fail to initialize TcpClient-[%s:%d].", ip, port));
		this.init(cleaner);
	}

	private boolean connect(String ip, int port){

		final List<Throwable> error = new ArrayList<>(1);
		final CountDownLatch latch = new CountDownLatch(1);

		client = vertx.createNetClient(new NetClientOptions().setTcpKeepAlive(true).setConnectTimeout(10)).connect(port, ip, res -> {
			if (res.succeeded()) {
				socket = res.result();
			} else {
				error.add(res.cause());
			}
			latch.countDown();
		});

		try {
			latch.await();
		} catch (InterruptedException ex) {
			logger.error("Thread interrupted while establishing connection.", ex);
			return false;
		}

		if (!error.isEmpty()) {
			logger.error("Exception raised while establishing connection", error.get(0));
			return false;
		}

		return true;
	}

	private void init(Consumer<TcpClient> cleaner) {
		socket.handler(buffer -> {

			Protocol.checkBuffer(buffer);
			long sequence = Protocol.readSequence(buffer);
			long marker = Protocol.readMarker(buffer);

			TcpRequest request = requestHolder.remove(sequence);

			boolean waiting = false;
			if (request != null) try {
				if (marker == Message.BUSINESS_MESSAGE) {
					waiting = request.success(
							Protocol.readMessage(buffer, request.clazz()), true);
				} else {

				}

			} catch (Exception e) {
				logger.error("Error occurred while parsing buffer.", e);
				waiting = request.fail(e);
			}

			if (waiting) vertx.cancelTimer(request.timer());
		});

		socket.closeHandler(ignored -> {
			final Throwable error = new SocketException("Socket closed.");
			this.requestHolder.forEach((seq, req)->req.fail(error));
			this.requestHolder.clear();
			cleaner.accept(this);
		});

		socket.exceptionHandler(logger::error);
	}

	private TcpRequest prepare(Class<? extends Message> clazz, long sequence, long timeout) {
		TcpRequest request = new TcpRequest(clazz,
				(tcpRequest -> vertx.setTimer(timeout, id->{
					if (tcpRequest.timeout(timeout)) {
						requestHolder.remove(sequence);
					}
				})));
		requestHolder.put(sequence, request);
		return request;
	}

	public <Request extends Message, Response extends Message>
	Message sendAndRecv(
			Request reqMsg, Class<Request> reqClazz,
			Class<Response> respClazz, long timeoutMills) throws Throwable {

		long reqSeq = sequenceHolder.incrementAndGet();
		Buffer buffer = Protocol.writeBuffer(reqSeq,
				Message.BUSINESS_MESSAGE, reqMsg, reqClazz);

		TcpRequest request = prepare(respClazz, reqSeq, timeoutMills);
		request.await(timeoutMills, ()->socket.write(buffer));

		switch (request.state()) {
			case TIMEOUT: case FAIL:
				throw (Throwable) request.result();
			case WAITING:
				throw new IllegalStateException("Request is still waiting.");
		}

		return request.result();
	}


}
