package dev.example.demonet.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "dev.example.demonet" })
public class GraphqlDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(GraphqlDemoApplication.class, args);
    }
}