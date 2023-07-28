package com.xlf.employee.fallback;

import com.xlf.employee.feign.RestroomFeignClient;
import com.xlf.restroom.pojo.Toilet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

// hystrix降级熔断
@Component
@Slf4j
public class RestroomFeignFallback implements RestroomFeignClient {

    @Override
    public List<Toilet> getAvailableToilet() {
        log.info(" hystrix----fallback");
        return null;
    }

    @Override
    public Toilet occpy(Long id) {
        log.info(" hystrix----fallback");
        return null;
    }

    @Override
    public Toilet release(Long id) {
        log.info(" hystrix----fallback");
        return null;
    }

    @Override
    public boolean checkAvailablility(Long id) {
        log.info(" hystrix----fallback");
        return false;
    }

    @Override
    public String demo() {
        log.info(" hystrix----fallback");
        return null;
    }

    @Override
    public ResponseEntity<byte[]> test(String input) {
        log.info(" hystrix----fallback");
        return null;
    }

    @Override
    public Toilet releaseTCC(Long id) {
        log.info(" hystrix----fallback");
        return null;
    }

    @Override
    public void hystrixDemo(Long count) throws InterruptedException {
        log.info(" hystrix----fallback");
    }
}
