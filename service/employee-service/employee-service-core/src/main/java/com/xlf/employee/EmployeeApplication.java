package com.xlf.employee;


import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
//@RibbonClient(name="employee-service", configuration= IRuleConfigBean.class)
@ComponentScan(basePackages = {"com.xlf"})
@EnableFeignClients(basePackages = {"com.xlf"})
// 打开熔断器
@EnableCircuitBreaker
@Slf4j
public class EmployeeApplication {


//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate(){
//        return new RestTemplate();
//    }

    // feign日志
    @Bean
    Logger.Level feignLoggerData(){
        return Logger.Level.FULL;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }
}
