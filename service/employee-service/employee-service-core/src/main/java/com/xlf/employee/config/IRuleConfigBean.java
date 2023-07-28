//package com.imooc.employee.config;
//
//import com.imooc.employee.loadbalancer.CustomeRule;
//import com.netflix.loadbalancer.IRule;
//import com.netflix.loadbalancer.RandomRule;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class IRuleConfigBean
//{
//    @Bean
//    @LoadBalanced //Ribbon 是客户端负载均衡的工具；
//    public RestTemplate getRestTemplate()
//    {
//        return new RestTemplate();
//    }
//
////    @Bean
////    public IRule myRule()
////    {
////        return new CustomeRule(); //自定义负载均衡规则
////    }
//
//    @Bean
//    public IRule myRule() {
//        // RandomRule 为随机策略
//        return  new RandomRule();
//    }
//}
