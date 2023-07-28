package com.xlf.restroom;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.xlf"})
public class RestroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestroomApplication.class, args);
    }
}

