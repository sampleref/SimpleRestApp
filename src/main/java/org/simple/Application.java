package org.simple;

import org.simple.controller.UserServiceRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceRestController.class, args);
    }
}
