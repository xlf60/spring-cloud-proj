package com.xlf.employee.service;

import com.xlf.employee.api.IEmployeeActivityService;
import com.xlf.employee.entity.EmployeeActivityEntity;
import com.xlf.employee.feign.RestroomFeignClient;
import com.xlf.employee.pojo.ActivityType;
import com.xlf.employee.pojo.EmployeeActivity;

import com.xlf.employee.dao.EmployeeActivityDao;
import com.xlf.restroom.pojo.Toilet;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeService implements IEmployeeActivityService {

    @Autowired
    private EmployeeActivityDao employeeActivityDao;

//    @Autowired
//    private RestTemplate restTemplate;

    //    @Autowired
    @Resource
//    private IRestroomService restroomService;
    private RestroomFeignClient restroomService;

    @Override
    @PostMapping("/toilet-break")
    @Transactional
    // AT方案
    @GlobalTransactional(name = "toilet-serv", rollbackFor = Exception.class)
    public EmployeeActivity useToilet(Long employeeId) {

//        System.out.println("employeeId = " + employeeId);
        int count = employeeActivityDao.countByEmployeeIdAndActivityTypeAndActive(
                employeeId, ActivityType.TOILET_BREAK, true);

        if (count > 0) {
            throw new RuntimeException("快拉");
        }

//        Toilet[] toilets = restTemplate.getForObject(
//                "http://restroom-service/toilet-service/checkAvailable/",
//                Toilet[].class);
//        if (ArrayUtils.isEmpty(toilets)) {
//            throw new RuntimeException("尿尿去");；w
//        }
        List<Toilet> toilets = restroomService.getAvailableToilet();

        if (CollectionUtils.isEmpty(toilets)) {
            throw new RuntimeException("尿尿去");
        }

        // 抢坑, 分布式事务
        Toilet toilet = restroomService.occpy(toilets.get(0).getId());


//        MultiValueMap<String, Object> args = new LinkedMultiValueMap<>();
//        assert toilets != null;
//        args.add("id", toilets[0].getId());
//        Toilet toilet = restTemplate.postForObject(
//                "http://restroom-service/toilet-service/occupy",
//                args,
//                Toilet.class
//        );

        // 保存如厕记录
        assert toilet != null;
        EmployeeActivityEntity toiletBreak = EmployeeActivityEntity.builder()
                .employeeId(employeeId)
                .active(true)
                .activityType(ActivityType.TOILET_BREAK)
                .resourceId(toilet.getId())
                .build();

        employeeActivityDao.save(toiletBreak);

        EmployeeActivity result = new EmployeeActivity();
        BeanUtils.copyProperties(toiletBreak, result);

        return result;
//        throw new RuntimeException("分布式");
    }

    @Override
    @Transactional
    @PostMapping("/done")
    // 开启全局事务
    // TCC方案
    @GlobalTransactional(name = "toilet-release", rollbackFor = Exception.class)
    public EmployeeActivity restoreToilet(Long activityId) {

        System.out.println("activityId = " + activityId);
        EmployeeActivityEntity record = employeeActivityDao.findById(activityId).orElseThrow(() -> new RuntimeException("record not found"));

        if (!record.isActive()) {
            throw new RuntimeException("activity is no longer active");
        }

//        restroomService.release(record.getResourceId());
        restroomService.releaseTCC(record.getResourceId());

//        MultiValueMap<String, Object> args = new LinkedMultiValueMap<>();
//        args.add("id", record.getResourceId());
//        restTemplate.postForObject(
//                "http://restroom-service/toilet-service/release",
//                args,
//                Toilet.class
//        );

        record.setActive(false);
        record.setEndTime(new Date());
        employeeActivityDao.save(record);

        EmployeeActivity result = new EmployeeActivity();
        BeanUtils.copyProperties(record, result);
        return result;

    }

    @Override
    @GetMapping("/demo11")
    @Transactional
    @Trace
    @Tag(key = "demo", value = "returnedObj")
    public void demo() {
        log.info("employee====demo");
        restroomService.demo();
    }

    @PostMapping("/test")
    public ResponseEntity<byte[]> test(Long count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(i);
        }
        restroomService.test(sb.toString());
        return null;
    }

    @PostMapping("/hystrixDemo")
    @HystrixCommand(
            commandKey = "hystrixTest",  // 全局唯一标识
            groupKey = "test",           // 全局服务分组，用于组织仪，统计信息  (默认值=类名)
            fallbackMethod = "hystrixDemoFallback"
//            threadPoolKey = "threadPoolA",
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "30"),
//                    @HystrixProperty(name = "maxQueueSize", value = "50"),
//                    // 统计维度
//            },
//            commandProperties = {
//
//            }
    )
    public String hystrixDemo(Long count) throws InterruptedException {
        log.info("hystrix====Demo");
        Thread.sleep(count * 1000);
//        restroomService.hystrixDemo(count);
        return count.toString();
    }

    public String hystrixDemoFallback(Long count) {
        log.info("hystrixDemoFallback");
        return "hystrixDemoFallback";
    }
}
