package org.grapheco.pandadb.network;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.0.0)",
        comments = "Source: query.proto")
public class PandaQueryServiceGrpc {

  private PandaQueryServiceGrpc() {}

  public static final String SERVICE_NAME = "org.grapheco.pandadb.network.PandaQueryService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<org.grapheco.pandadb.network.Query.QueryRequest,
          org.grapheco.pandadb.network.Query.QueryResponse> METHOD_QUERY =
          io.grpc.MethodDescriptor.create(
                  io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
                  generateFullMethodName(
                          "org.grapheco.pandadb.network.PandaQueryService", "Query"),
                  io.grpc.protobuf.ProtoUtils.marshaller(org.grapheco.pandadb.network.Query.QueryRequest.getDefaultInstance()),
                  io.grpc.protobuf.ProtoUtils.marshaller(org.grapheco.pandadb.network.Query.QueryResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PandaQueryServiceStub newStub(io.grpc.Channel channel) {
    return new PandaQueryServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PandaQueryServiceBlockingStub newBlockingStub(
          io.grpc.Channel channel) {
    return new PandaQueryServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static PandaQueryServiceFutureStub newFutureStub(
          io.grpc.Channel channel) {
    return new PandaQueryServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class PandaQueryServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void query(org.grapheco.pandadb.network.Query.QueryRequest request,
                      io.grpc.stub.StreamObserver<org.grapheco.pandadb.network.Query.QueryResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_QUERY, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
              .addMethod(
                      METHOD_QUERY,
                      asyncServerStreamingCall(
                              new MethodHandlers<
                                      org.grapheco.pandadb.network.Query.QueryRequest,
                                      org.grapheco.pandadb.network.Query.QueryResponse>(
                                      this, METHODID_QUERY)))
              .build();
    }
  }

  /**
   */
  public static final class PandaQueryServiceStub extends io.grpc.stub.AbstractStub<PandaQueryServiceStub> {
    private PandaQueryServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PandaQueryServiceStub(io.grpc.Channel channel,
                                  io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PandaQueryServiceStub build(io.grpc.Channel channel,
                                          io.grpc.CallOptions callOptions) {
      return new PandaQueryServiceStub(channel, callOptions);
    }

    /**
     */
    public void query(org.grapheco.pandadb.network.Query.QueryRequest request,
                      io.grpc.stub.StreamObserver<org.grapheco.pandadb.network.Query.QueryResponse> responseObserver) {
      asyncServerStreamingCall(
              getChannel().newCall(METHOD_QUERY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PandaQueryServiceBlockingStub extends io.grpc.stub.AbstractStub<PandaQueryServiceBlockingStub> {
    private PandaQueryServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PandaQueryServiceBlockingStub(io.grpc.Channel channel,
                                          io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PandaQueryServiceBlockingStub build(io.grpc.Channel channel,
                                                  io.grpc.CallOptions callOptions) {
      return new PandaQueryServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<org.grapheco.pandadb.network.Query.QueryResponse> query(
            org.grapheco.pandadb.network.Query.QueryRequest request) {
      return blockingServerStreamingCall(
              getChannel(), METHOD_QUERY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PandaQueryServiceFutureStub extends io.grpc.stub.AbstractStub<PandaQueryServiceFutureStub> {
    private PandaQueryServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PandaQueryServiceFutureStub(io.grpc.Channel channel,
                                        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PandaQueryServiceFutureStub build(io.grpc.Channel channel,
                                                io.grpc.CallOptions callOptions) {
      return new PandaQueryServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_QUERY = 0;

  private static class MethodHandlers<Req, Resp> implements
          io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PandaQueryServiceImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(PandaQueryServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_QUERY:
          serviceImpl.query((org.grapheco.pandadb.network.Query.QueryRequest) request,
                  (io.grpc.stub.StreamObserver<org.grapheco.pandadb.network.Query.QueryResponse>) responseObserver);
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

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
            METHOD_QUERY);
  }

}
