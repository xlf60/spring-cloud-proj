package com.xlf.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;


@EnableDiscoveryClient
@EnableTurbine
@EnableAutoConfiguration
public class TurbineServer {

    public static void main(String[] args) {
        SpringApplication.run(TurbineServer.class, args);
    }
}
