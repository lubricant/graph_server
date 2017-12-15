package com.soga.social.service;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.8.0)",
    comments = "Source: social_graph.proto")
public final class SocialGraphServiceGrpc {

  private SocialGraphServiceGrpc() {}

  public static final String SERVICE_NAME = "social.SocialGraphService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCreatePersonMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.soga.social.service.PersonKey,
      com.soga.social.service.Result> METHOD_CREATE_PERSON = getCreatePersonMethod();

  private static volatile io.grpc.MethodDescriptor<com.soga.social.service.PersonKey,
      com.soga.social.service.Result> getCreatePersonMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.soga.social.service.PersonKey,
      com.soga.social.service.Result> getCreatePersonMethod() {
    io.grpc.MethodDescriptor<com.soga.social.service.PersonKey, com.soga.social.service.Result> getCreatePersonMethod;
    if ((getCreatePersonMethod = SocialGraphServiceGrpc.getCreatePersonMethod) == null) {
      synchronized (SocialGraphServiceGrpc.class) {
        if ((getCreatePersonMethod = SocialGraphServiceGrpc.getCreatePersonMethod) == null) {
          SocialGraphServiceGrpc.getCreatePersonMethod = getCreatePersonMethod = 
              io.grpc.MethodDescriptor.<com.soga.social.service.PersonKey, com.soga.social.service.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "social.SocialGraphService", "createPerson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.PersonKey.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.Result.getDefaultInstance()))
                  .setSchemaDescriptor(new SocialGraphServiceMethodDescriptorSupplier("createPerson"))
                  .build();
          }
        }
     }
     return getCreatePersonMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRemovePersonMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.soga.social.service.PersonKey,
      com.soga.social.service.PersonConn> METHOD_REMOVE_PERSON = getRemovePersonMethod();

  private static volatile io.grpc.MethodDescriptor<com.soga.social.service.PersonKey,
      com.soga.social.service.PersonConn> getRemovePersonMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.soga.social.service.PersonKey,
      com.soga.social.service.PersonConn> getRemovePersonMethod() {
    io.grpc.MethodDescriptor<com.soga.social.service.PersonKey, com.soga.social.service.PersonConn> getRemovePersonMethod;
    if ((getRemovePersonMethod = SocialGraphServiceGrpc.getRemovePersonMethod) == null) {
      synchronized (SocialGraphServiceGrpc.class) {
        if ((getRemovePersonMethod = SocialGraphServiceGrpc.getRemovePersonMethod) == null) {
          SocialGraphServiceGrpc.getRemovePersonMethod = getRemovePersonMethod = 
              io.grpc.MethodDescriptor.<com.soga.social.service.PersonKey, com.soga.social.service.PersonConn>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "social.SocialGraphService", "removePerson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.PersonKey.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.PersonConn.getDefaultInstance()))
                  .setSchemaDescriptor(new SocialGraphServiceMethodDescriptorSupplier("removePerson"))
                  .build();
          }
        }
     }
     return getRemovePersonMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getConnectPersonMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.soga.social.service.ConnectionKey,
      com.soga.social.service.Result> METHOD_CONNECT_PERSON = getConnectPersonMethod();

  private static volatile io.grpc.MethodDescriptor<com.soga.social.service.ConnectionKey,
      com.soga.social.service.Result> getConnectPersonMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.soga.social.service.ConnectionKey,
      com.soga.social.service.Result> getConnectPersonMethod() {
    io.grpc.MethodDescriptor<com.soga.social.service.ConnectionKey, com.soga.social.service.Result> getConnectPersonMethod;
    if ((getConnectPersonMethod = SocialGraphServiceGrpc.getConnectPersonMethod) == null) {
      synchronized (SocialGraphServiceGrpc.class) {
        if ((getConnectPersonMethod = SocialGraphServiceGrpc.getConnectPersonMethod) == null) {
          SocialGraphServiceGrpc.getConnectPersonMethod = getConnectPersonMethod = 
              io.grpc.MethodDescriptor.<com.soga.social.service.ConnectionKey, com.soga.social.service.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "social.SocialGraphService", "connectPerson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.ConnectionKey.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.Result.getDefaultInstance()))
                  .setSchemaDescriptor(new SocialGraphServiceMethodDescriptorSupplier("connectPerson"))
                  .build();
          }
        }
     }
     return getConnectPersonMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDisconnectPersonMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.soga.social.service.ConnectionKey,
      com.soga.social.service.ConnPerson> METHOD_DISCONNECT_PERSON = getDisconnectPersonMethod();

  private static volatile io.grpc.MethodDescriptor<com.soga.social.service.ConnectionKey,
      com.soga.social.service.ConnPerson> getDisconnectPersonMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.soga.social.service.ConnectionKey,
      com.soga.social.service.ConnPerson> getDisconnectPersonMethod() {
    io.grpc.MethodDescriptor<com.soga.social.service.ConnectionKey, com.soga.social.service.ConnPerson> getDisconnectPersonMethod;
    if ((getDisconnectPersonMethod = SocialGraphServiceGrpc.getDisconnectPersonMethod) == null) {
      synchronized (SocialGraphServiceGrpc.class) {
        if ((getDisconnectPersonMethod = SocialGraphServiceGrpc.getDisconnectPersonMethod) == null) {
          SocialGraphServiceGrpc.getDisconnectPersonMethod = getDisconnectPersonMethod = 
              io.grpc.MethodDescriptor.<com.soga.social.service.ConnectionKey, com.soga.social.service.ConnPerson>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "social.SocialGraphService", "disconnectPerson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.ConnectionKey.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.ConnPerson.getDefaultInstance()))
                  .setSchemaDescriptor(new SocialGraphServiceMethodDescriptorSupplier("disconnectPerson"))
                  .build();
          }
        }
     }
     return getDisconnectPersonMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getUpdatePersonMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.soga.social.service.Person,
      com.soga.social.service.Result> METHOD_UPDATE_PERSON = getUpdatePersonMethod();

  private static volatile io.grpc.MethodDescriptor<com.soga.social.service.Person,
      com.soga.social.service.Result> getUpdatePersonMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.soga.social.service.Person,
      com.soga.social.service.Result> getUpdatePersonMethod() {
    io.grpc.MethodDescriptor<com.soga.social.service.Person, com.soga.social.service.Result> getUpdatePersonMethod;
    if ((getUpdatePersonMethod = SocialGraphServiceGrpc.getUpdatePersonMethod) == null) {
      synchronized (SocialGraphServiceGrpc.class) {
        if ((getUpdatePersonMethod = SocialGraphServiceGrpc.getUpdatePersonMethod) == null) {
          SocialGraphServiceGrpc.getUpdatePersonMethod = getUpdatePersonMethod = 
              io.grpc.MethodDescriptor.<com.soga.social.service.Person, com.soga.social.service.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "social.SocialGraphService", "updatePerson"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.Person.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.Result.getDefaultInstance()))
                  .setSchemaDescriptor(new SocialGraphServiceMethodDescriptorSupplier("updatePerson"))
                  .build();
          }
        }
     }
     return getUpdatePersonMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getUpdateConnectionMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.soga.social.service.Connection,
      com.soga.social.service.Result> METHOD_UPDATE_CONNECTION = getUpdateConnectionMethod();

  private static volatile io.grpc.MethodDescriptor<com.soga.social.service.Connection,
      com.soga.social.service.Result> getUpdateConnectionMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.soga.social.service.Connection,
      com.soga.social.service.Result> getUpdateConnectionMethod() {
    io.grpc.MethodDescriptor<com.soga.social.service.Connection, com.soga.social.service.Result> getUpdateConnectionMethod;
    if ((getUpdateConnectionMethod = SocialGraphServiceGrpc.getUpdateConnectionMethod) == null) {
      synchronized (SocialGraphServiceGrpc.class) {
        if ((getUpdateConnectionMethod = SocialGraphServiceGrpc.getUpdateConnectionMethod) == null) {
          SocialGraphServiceGrpc.getUpdateConnectionMethod = getUpdateConnectionMethod = 
              io.grpc.MethodDescriptor.<com.soga.social.service.Connection, com.soga.social.service.Result>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "social.SocialGraphService", "updateConnection"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.Connection.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.Result.getDefaultInstance()))
                  .setSchemaDescriptor(new SocialGraphServiceMethodDescriptorSupplier("updateConnection"))
                  .build();
          }
        }
     }
     return getUpdateConnectionMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getTraverseGraphMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.soga.social.service.TraversalDesc,
      com.soga.social.service.TraversalTree> METHOD_TRAVERSE_GRAPH = getTraverseGraphMethod();

  private static volatile io.grpc.MethodDescriptor<com.soga.social.service.TraversalDesc,
      com.soga.social.service.TraversalTree> getTraverseGraphMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.soga.social.service.TraversalDesc,
      com.soga.social.service.TraversalTree> getTraverseGraphMethod() {
    io.grpc.MethodDescriptor<com.soga.social.service.TraversalDesc, com.soga.social.service.TraversalTree> getTraverseGraphMethod;
    if ((getTraverseGraphMethod = SocialGraphServiceGrpc.getTraverseGraphMethod) == null) {
      synchronized (SocialGraphServiceGrpc.class) {
        if ((getTraverseGraphMethod = SocialGraphServiceGrpc.getTraverseGraphMethod) == null) {
          SocialGraphServiceGrpc.getTraverseGraphMethod = getTraverseGraphMethod = 
              io.grpc.MethodDescriptor.<com.soga.social.service.TraversalDesc, com.soga.social.service.TraversalTree>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "social.SocialGraphService", "traverseGraph"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.TraversalDesc.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.soga.social.service.TraversalTree.getDefaultInstance()))
                  .setSchemaDescriptor(new SocialGraphServiceMethodDescriptorSupplier("traverseGraph"))
                  .build();
          }
        }
     }
     return getTraverseGraphMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SocialGraphServiceStub newStub(io.grpc.Channel channel) {
    return new SocialGraphServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SocialGraphServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SocialGraphServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SocialGraphServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SocialGraphServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class SocialGraphServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void createPerson(com.soga.social.service.PersonKey request,
        io.grpc.stub.StreamObserver<com.soga.social.service.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getCreatePersonMethod(), responseObserver);
    }

    /**
     */
    public void removePerson(com.soga.social.service.PersonKey request,
        io.grpc.stub.StreamObserver<com.soga.social.service.PersonConn> responseObserver) {
      asyncUnimplementedUnaryCall(getRemovePersonMethod(), responseObserver);
    }

    /**
     */
    public void connectPerson(com.soga.social.service.ConnectionKey request,
        io.grpc.stub.StreamObserver<com.soga.social.service.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getConnectPersonMethod(), responseObserver);
    }

    /**
     */
    public void disconnectPerson(com.soga.social.service.ConnectionKey request,
        io.grpc.stub.StreamObserver<com.soga.social.service.ConnPerson> responseObserver) {
      asyncUnimplementedUnaryCall(getDisconnectPersonMethod(), responseObserver);
    }

    /**
     */
    public void updatePerson(com.soga.social.service.Person request,
        io.grpc.stub.StreamObserver<com.soga.social.service.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdatePersonMethod(), responseObserver);
    }

    /**
     */
    public void updateConnection(com.soga.social.service.Connection request,
        io.grpc.stub.StreamObserver<com.soga.social.service.Result> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateConnectionMethod(), responseObserver);
    }

    /**
     */
    public void traverseGraph(com.soga.social.service.TraversalDesc request,
        io.grpc.stub.StreamObserver<com.soga.social.service.TraversalTree> responseObserver) {
      asyncUnimplementedUnaryCall(getTraverseGraphMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCreatePersonMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.soga.social.service.PersonKey,
                com.soga.social.service.Result>(
                  this, METHODID_CREATE_PERSON)))
          .addMethod(
            getRemovePersonMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.soga.social.service.PersonKey,
                com.soga.social.service.PersonConn>(
                  this, METHODID_REMOVE_PERSON)))
          .addMethod(
            getConnectPersonMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.soga.social.service.ConnectionKey,
                com.soga.social.service.Result>(
                  this, METHODID_CONNECT_PERSON)))
          .addMethod(
            getDisconnectPersonMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.soga.social.service.ConnectionKey,
                com.soga.social.service.ConnPerson>(
                  this, METHODID_DISCONNECT_PERSON)))
          .addMethod(
            getUpdatePersonMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.soga.social.service.Person,
                com.soga.social.service.Result>(
                  this, METHODID_UPDATE_PERSON)))
          .addMethod(
            getUpdateConnectionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.soga.social.service.Connection,
                com.soga.social.service.Result>(
                  this, METHODID_UPDATE_CONNECTION)))
          .addMethod(
            getTraverseGraphMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.soga.social.service.TraversalDesc,
                com.soga.social.service.TraversalTree>(
                  this, METHODID_TRAVERSE_GRAPH)))
          .build();
    }
  }

  /**
   */
  public static final class SocialGraphServiceStub extends io.grpc.stub.AbstractStub<SocialGraphServiceStub> {
    private SocialGraphServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SocialGraphServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SocialGraphServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SocialGraphServiceStub(channel, callOptions);
    }

    /**
     */
    public void createPerson(com.soga.social.service.PersonKey request,
        io.grpc.stub.StreamObserver<com.soga.social.service.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreatePersonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removePerson(com.soga.social.service.PersonKey request,
        io.grpc.stub.StreamObserver<com.soga.social.service.PersonConn> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemovePersonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void connectPerson(com.soga.social.service.ConnectionKey request,
        io.grpc.stub.StreamObserver<com.soga.social.service.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getConnectPersonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void disconnectPerson(com.soga.social.service.ConnectionKey request,
        io.grpc.stub.StreamObserver<com.soga.social.service.ConnPerson> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDisconnectPersonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updatePerson(com.soga.social.service.Person request,
        io.grpc.stub.StreamObserver<com.soga.social.service.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdatePersonMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateConnection(com.soga.social.service.Connection request,
        io.grpc.stub.StreamObserver<com.soga.social.service.Result> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateConnectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void traverseGraph(com.soga.social.service.TraversalDesc request,
        io.grpc.stub.StreamObserver<com.soga.social.service.TraversalTree> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getTraverseGraphMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SocialGraphServiceBlockingStub extends io.grpc.stub.AbstractStub<SocialGraphServiceBlockingStub> {
    private SocialGraphServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SocialGraphServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SocialGraphServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SocialGraphServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.soga.social.service.Result createPerson(com.soga.social.service.PersonKey request) {
      return blockingUnaryCall(
          getChannel(), getCreatePersonMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.soga.social.service.PersonConn removePerson(com.soga.social.service.PersonKey request) {
      return blockingUnaryCall(
          getChannel(), getRemovePersonMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.soga.social.service.Result connectPerson(com.soga.social.service.ConnectionKey request) {
      return blockingUnaryCall(
          getChannel(), getConnectPersonMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.soga.social.service.ConnPerson disconnectPerson(com.soga.social.service.ConnectionKey request) {
      return blockingUnaryCall(
          getChannel(), getDisconnectPersonMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.soga.social.service.Result updatePerson(com.soga.social.service.Person request) {
      return blockingUnaryCall(
          getChannel(), getUpdatePersonMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.soga.social.service.Result updateConnection(com.soga.social.service.Connection request) {
      return blockingUnaryCall(
          getChannel(), getUpdateConnectionMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.soga.social.service.TraversalTree traverseGraph(com.soga.social.service.TraversalDesc request) {
      return blockingUnaryCall(
          getChannel(), getTraverseGraphMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SocialGraphServiceFutureStub extends io.grpc.stub.AbstractStub<SocialGraphServiceFutureStub> {
    private SocialGraphServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SocialGraphServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SocialGraphServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SocialGraphServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.soga.social.service.Result> createPerson(
        com.soga.social.service.PersonKey request) {
      return futureUnaryCall(
          getChannel().newCall(getCreatePersonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.soga.social.service.PersonConn> removePerson(
        com.soga.social.service.PersonKey request) {
      return futureUnaryCall(
          getChannel().newCall(getRemovePersonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.soga.social.service.Result> connectPerson(
        com.soga.social.service.ConnectionKey request) {
      return futureUnaryCall(
          getChannel().newCall(getConnectPersonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.soga.social.service.ConnPerson> disconnectPerson(
        com.soga.social.service.ConnectionKey request) {
      return futureUnaryCall(
          getChannel().newCall(getDisconnectPersonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.soga.social.service.Result> updatePerson(
        com.soga.social.service.Person request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdatePersonMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.soga.social.service.Result> updateConnection(
        com.soga.social.service.Connection request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateConnectionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.soga.social.service.TraversalTree> traverseGraph(
        com.soga.social.service.TraversalDesc request) {
      return futureUnaryCall(
          getChannel().newCall(getTraverseGraphMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_PERSON = 0;
  private static final int METHODID_REMOVE_PERSON = 1;
  private static final int METHODID_CONNECT_PERSON = 2;
  private static final int METHODID_DISCONNECT_PERSON = 3;
  private static final int METHODID_UPDATE_PERSON = 4;
  private static final int METHODID_UPDATE_CONNECTION = 5;
  private static final int METHODID_TRAVERSE_GRAPH = 6;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SocialGraphServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SocialGraphServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_PERSON:
          serviceImpl.createPerson((com.soga.social.service.PersonKey) request,
              (io.grpc.stub.StreamObserver<com.soga.social.service.Result>) responseObserver);
          break;
        case METHODID_REMOVE_PERSON:
          serviceImpl.removePerson((com.soga.social.service.PersonKey) request,
              (io.grpc.stub.StreamObserver<com.soga.social.service.PersonConn>) responseObserver);
          break;
        case METHODID_CONNECT_PERSON:
          serviceImpl.connectPerson((com.soga.social.service.ConnectionKey) request,
              (io.grpc.stub.StreamObserver<com.soga.social.service.Result>) responseObserver);
          break;
        case METHODID_DISCONNECT_PERSON:
          serviceImpl.disconnectPerson((com.soga.social.service.ConnectionKey) request,
              (io.grpc.stub.StreamObserver<com.soga.social.service.ConnPerson>) responseObserver);
          break;
        case METHODID_UPDATE_PERSON:
          serviceImpl.updatePerson((com.soga.social.service.Person) request,
              (io.grpc.stub.StreamObserver<com.soga.social.service.Result>) responseObserver);
          break;
        case METHODID_UPDATE_CONNECTION:
          serviceImpl.updateConnection((com.soga.social.service.Connection) request,
              (io.grpc.stub.StreamObserver<com.soga.social.service.Result>) responseObserver);
          break;
        case METHODID_TRAVERSE_GRAPH:
          serviceImpl.traverseGraph((com.soga.social.service.TraversalDesc) request,
              (io.grpc.stub.StreamObserver<com.soga.social.service.TraversalTree>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class SocialGraphServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SocialGraphServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.soga.social.service.SocialGraph.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SocialGraphService");
    }
  }

  private static final class SocialGraphServiceFileDescriptorSupplier
      extends SocialGraphServiceBaseDescriptorSupplier {
    SocialGraphServiceFileDescriptorSupplier() {}
  }

  private static final class SocialGraphServiceMethodDescriptorSupplier
      extends SocialGraphServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SocialGraphServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SocialGraphServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SocialGraphServiceFileDescriptorSupplier())
              .addMethod(getCreatePersonMethod())
              .addMethod(getRemovePersonMethod())
              .addMethod(getConnectPersonMethod())
              .addMethod(getDisconnectPersonMethod())
              .addMethod(getUpdatePersonMethod())
              .addMethod(getUpdateConnectionMethod())
              .addMethod(getTraverseGraphMethod())
              .build();
        }
      }
    }
    return result;
  }
}
