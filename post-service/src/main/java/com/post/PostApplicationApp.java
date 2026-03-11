package com.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PostApplicationApp {
    public static void main(String[] args) {
        SpringApplication.run(PostApplicationApp.class, args);
    }
}
