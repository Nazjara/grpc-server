package com.nazjara;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class GrpcServerApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(GrpcServerApplication.class, args);

        log.info("Starting server on port 8080...");

        Server server = ServerBuilder
                .forPort(8080)
                .addService(new ExampleServiceImpl()).build();
        server.start();

        log.info("Server started");

        server.awaitTermination();
    }
}