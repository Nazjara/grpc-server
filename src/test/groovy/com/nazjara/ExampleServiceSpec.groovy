package com.nazjara

import io.grpc.stub.StreamObserver
import spock.lang.Specification

class ExampleServiceSpec extends Specification {

    StreamObserver<Response> responseObserver = Mock()
    ExampleServiceImpl exampleService = new ExampleServiceImpl()
    StreamObserver<Request> requestObserver

    def "process singleRpc"() {
        when:
        exampleService.singleRpc(Request.newBuilder().setValue("value").build(), responseObserver)

        then:
        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response instanceof Response
            assert response.value == "eulav"
        }

        1 * responseObserver.onCompleted()
    }

    def "process serverSideStreamingRpc"() {
        when:
        exampleService.serverSideStreamingRpc(Request.newBuilder().setValue("value").build(), responseObserver)

        then:
        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "v"
        }

        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "a"
        }

        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "l"
        }

        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "u"
        }

        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "e"
        }

        1 * responseObserver.onCompleted()
    }

    def "process clientSideStreamingRpc"() {
        requestObserver = exampleService.clientSideStreamingRpc(responseObserver)

        when:
        requestObserver.onNext(Request.newBuilder().setValue("e").build())
        requestObserver.onNext(Request.newBuilder().setValue("u").build())
        requestObserver.onNext(Request.newBuilder().setValue("l").build())
        requestObserver.onNext(Request.newBuilder().setValue("a").build())
        requestObserver.onNext(Request.newBuilder().setValue("v").build())
        requestObserver.onCompleted()

        then:
        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "value"
        }

        1 * responseObserver.onCompleted()
    }

    def "process bidirectionalStreamingRpc"() {
        requestObserver = exampleService.bidirectionalStreamingRpc(responseObserver)

        when:
        requestObserver.onNext(Request.newBuilder().setValue("1eulav").build())
        requestObserver.onNext(Request.newBuilder().setValue("2eulav").build())
        requestObserver.onNext(Request.newBuilder().setValue("3eulav").build())
        requestObserver.onNext(Request.newBuilder().setValue("4eulav").build())
        requestObserver.onNext(Request.newBuilder().setValue("5eulav").build())
        requestObserver.onCompleted()

        then:
        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "value1"
        }

        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "value2"
        }

        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "value3"
        }

        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "value4"
        }

        1 * responseObserver.onNext(_) >> {
            def response = it[0]
            assert response.value == "value5"
        }

        1 * responseObserver.onCompleted()
    }
}