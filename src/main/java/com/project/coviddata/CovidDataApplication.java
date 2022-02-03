package com.project.coviddata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CovidDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(CovidDataApplication.class, args);
    }

}
