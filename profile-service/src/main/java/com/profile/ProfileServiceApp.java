package com.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProfileServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(ProfileServiceApp.class, args);
    }

}
