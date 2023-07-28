package com.xlf.platform.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RedisLimiterConfiguration {

    // 限流维度
    @Bean
    @Primary
    public KeyResolver redisLimiterKey(){
        return exchange -> Mono.just(
//                exchange.getRequest().getQueryParams().getFirst("customerId")
                Objects.requireNonNull(exchange.getRequest()
                                .getRemoteAddress())
                        .getAddress()
                        .getHostAddress()
        );
    }

    @Bean(name = "restroomRateLimiter")
    @Primary
    public RedisRateLimiter redisLimiter(){
        // 令牌桶算法
        return new RedisRateLimiter(1,1);
    }


    @Bean(name = "employeeRateLimiter")
    public RedisRateLimiter employeeLimiter(){
        // 令牌桶算法
        return new RedisRateLimiter(10,10);
    }
}
