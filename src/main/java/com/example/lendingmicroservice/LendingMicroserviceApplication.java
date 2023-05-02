package com.example.lendingmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class LendingMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LendingMicroserviceApplication.class, args);
    }

}
