package com.xlf.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User
                .withUsername("imooc1")
                .password("{bcrypt}" + new BCryptPasswordEncoder().encode("XLF"))
                .authorities("AMDIN", "USER")
                .roles("ADMIN", "USER")
                .build()
        );
        return userDetailsManager;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 访问路径发权限控制
       http.authorizeRequests()
               .antMatchers(HttpMethod.OPTIONS).permitAll()
               .antMatchers("/oauth/**").permitAll()
               .antMatchers("/a/***").hasRole("abc")
               .antMatchers("/b/**").access("hasRole('USER') and hasRole('ADMIN')")
               .anyRequest().permitAll()

               .and().httpBasic()
               .and().csrf().disable();

    }
}
