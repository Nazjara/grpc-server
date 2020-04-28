package com.nazjara;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExampleServiceImpl extends ExampleServiceGrpc.ExampleServiceImplBase {

    //  return reverted String
    @Override
    public void singleRpc(Request request, StreamObserver<Response> responseObserver) {
        log.info("singleRpc request received: " + request.getValue());
        Response response = Response.newBuilder()
                .setValue(new StringBuilder(request.getValue()).reverse().toString()).build();

        log.info("returning " + response.getValue() + " as a singleRpc response");
        responseObserver.onNext(response);
        log.info("singleRpc request processed");
        responseObserver.onCompleted();
    }

    //  return list of characters for string
    @Override
    public void serverSideStreamingRpc(Request request, StreamObserver<Response> responseObserver) {
        log.info("serverSideStreamingRpc request received: " + request.getValue());

        for(String value : request.getValue().split("")) {
            log.info("returning " + value + " as a part of serverSideStreamingRpc response");
            responseObserver.onNext(Response.newBuilder().setValue(value).build());
        }

        log.info("serverSideStreamingRpc request processed");
        responseObserver.onCompleted();
    }

    //  gather characters to a string, revert and return
    @Override
    public StreamObserver<Request> clientSideStreamingRpc(final StreamObserver<Response> responseObserver) {
        log.info("clientSideStreamingRpc request received");

        return new StreamObserver<>() {
            final StringBuilder responseString = new StringBuilder();

            @Override
            public void onNext(Request value) {
                log.info("processing " + value + " as a part of clientSideStreamingRpc request");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                responseString.append(value.getValue());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error happened during clientSideStreamingRpc process", t);
            }

            @Override
            public void onCompleted() {
                String response = responseString.reverse().toString();

                log.info("returning " + response + " as a clientSideStreamingRpc response");
                responseObserver.onNext(Response.newBuilder().setValue(response).build());

                log.info("clientSideStreamingRpc request processed");
                responseObserver.onCompleted();
            }
        };
    }

    // send list of strings, return list of reverted strings
    @Override
    public StreamObserver<Request> bidirectionalStreamingRpc(StreamObserver<Response> responseObserver) {
        log.info("bidirectionalStreamingRpc request received");

        return new StreamObserver<>() {
            @Override
            public void onNext(Request value) {
                log.info("processing " + value.getValue() + " as a part of bidirectionalStreamingRpc request");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String response = new StringBuilder(value.getValue()).reverse().toString();

                log.info("returning " + response + " as a part of bidirectionalStreamingRpc response");

                responseObserver.onNext(Response.newBuilder().setValue(response).build());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error happened during clientSideStreamingRpc process", t);
            }

            @Override
            public void onCompleted() {
                log.info("bidirectionalStreamingRpc request processed");
                responseObserver.onCompleted();
            }
        };
    }
}