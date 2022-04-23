package com.example.weathermanspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeathermanSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeathermanSpringApplication.class, args);
    }

}
