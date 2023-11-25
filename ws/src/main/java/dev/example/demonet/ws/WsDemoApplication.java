package dev.example.demonet.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "dev.example.demonet" })
public class WsDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WsDemoApplication.class, args);
    }
}