package com.xlf.employee.feign;


import com.xlf.employee.fallback.RestroomFeignFallback;
import com.xlf.restroom.pojo.Toilet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

     @FeignClient(value = "restroom-service", fallback = RestroomFeignFallback.class)
//public interface RestroomFeignClient extends IRestroomService {
public interface RestroomFeignClient {

     @GetMapping("/toilet-service/checkAvailable")
     List<Toilet> getAvailableToilet();

     @PostMapping("/toilet-service/occpy")
     Toilet occpy(@RequestParam("id") Long id);

     @PostMapping( "/toilet-service/release")
     Toilet release(@RequestParam("id") Long id);

     @GetMapping("/toilet-service/checkAvailablility")
     boolean checkAvailablility(@RequestParam("id") Long id);

     @GetMapping("/toilet-service/demo")
     String demo();

     @GetMapping("/toilet-service/test")
     ResponseEntity<byte[]> test(@RequestParam("input") String input);

     @PostMapping( "/toilet-service/releaseTCC")
     Toilet releaseTCC(@RequestParam("id") Long id);

     @PostMapping("/toilet-service/hystrixDemo")
     void  hystrixDemo(Long count) throws InterruptedException;
}
