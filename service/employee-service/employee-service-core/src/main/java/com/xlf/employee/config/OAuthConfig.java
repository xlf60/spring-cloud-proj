//package com.imooc.employee.config;
//
//import feign.RequestInterceptor;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//
//@EnableOAuth2Client
//@EnableConfigurationProperties
//@Configuration
//public class OAuthConfig {
//
//    @Bean
//    @ConfigurationProperties(prefix = "security.oauth2.client")
//    public ClientCredentialsResourceDetails clientCredentialsResourceDetails(){
//        return new ClientCredentialsResourceDetails();
//    }
//
//    @Bean
//    public RequestInterceptor oAuth2FeignRequestInterceptor(ClientCredentialsResourceDetails clientCredentialsResourceDetails){
//        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails);
//    }
//
//    @Bean
//    public OAuth2RestTemplate restTemplate(ClientCredentialsResourceDetails clientCredentialsResourceDetails){
//        return new OAuth2RestTemplate(clientCredentialsResourceDetails);
//    }
//}
