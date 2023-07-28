package com.xlf.platform.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class RouteConfiguration {

    @Autowired
    private KeyResolver hostAddressKeyResolver;


    @Autowired
    @Qualifier("restroomRateLimiter")
    private RateLimiter restroomRateLimiter;

    @Bean
    @Order(-1)   // 权重配置，越小越高
    public GlobalFilter globalFilter() {
        return ((exchange, chain) -> {
            // PRE
            log.info("PRE  filter");
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                                // POST logic
                                log.info("POST tpye filter");
                            }
                    ));
            // POST
        });
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 3个部分
                .route(rouute -> rouute
                                // 开始
                        .path("/toilet-service/**")
                        .filters(f -> f.requestRateLimiter(
                                        limiter -> {
                                            limiter.setKeyResolver(hostAddressKeyResolver);
                                            limiter.setRateLimiter(restroomRateLimiter);
                                            limiter.setStatusCode(HttpStatus.BAD_GATEWAY);
                                        }
                                ).hystrix(filter -> {
                                    filter.setName("fallbackName");  // 与yml文件对应
                                    filter.setFallbackUri("forward:/global-errors");
                                })
                     )
                                    // 时间
//                        .or().before(ZonedDateTime.now())
//                        .or().after(ZonedDateTime.now())
//                        .or().between()
                                // 路由到
                                .uri("lb://restroom-service")
                )
                .route(rouute -> rouute
                                // 开始
                                .path("/employee/**")
//                        .filters("")
                                // 路由到 (负载均衡)
                                .uri("lb://employee-service")
                )

                .build();
    }

    @RestController
    public class ErrorHadnder{

        @RequestMapping("/global-errors")
        public String errorMethod(){
            return "Hystrix fallback";
        }
    }
}
