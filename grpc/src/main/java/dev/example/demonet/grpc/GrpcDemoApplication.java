package dev.example.demonet.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "dev.example.demonet" })
public class GrpcDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrpcDemoApplication.class, args);
    }
}
