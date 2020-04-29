FROM alpine:latest
WORKDIR /opt/grpc/
COPY target/grpc-server-*.jar .
RUN apk add --no-cache openjdk11  && \
    mv grpc-server-*.jar grpc-server.jar
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/grpc/grpc-server.jar"]
VOLUME /var/lib/grpc/config-repo
EXPOSE 8080