package com.xlf.restroom.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Lists;
import com.xlf.restroom.converter.ToiletConverter;
import com.xlf.restroom.dao.ToiletDao;
import com.xlf.restroom.entity.ToiletEntity;
import com.xlf.restroom.pojo.Toilet;
import com.xlf.restroom.util.SentinelRule;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("toilet-service")
@RefreshScope   // 动态刷新
//public class RestroomService implements IRestroomService {

// Tcc

public class RestroomService implements IRestroomTccService {

    @Autowired
    private ToiletDao toiletDao;

    @Value("${restroom.title}")
    private String title;

    @Value("${restroom.disable:false}")
    private boolean disableRestroom;

    /**
     * Sentinel
     */
    @Override
    @GetMapping("/checkAvailable")
    @SentinelResource(value = "checkAvailable", fallback = "getAvailableToiletFallback")
    public List<Toilet> getAvailableToilet() {

        // 通过代码定义流量控制规则
        SentinelRule.initFlowRules("checkAvailable");

        // 初始化熔断策略 ( 降级规则 )
        SentinelRule.initDegradeRule("checkAvailableFallback");

        log.info("checkAvailable, title={}", title);

        if (disableRestroom) {
            log.info("restroom is 不可用! 进入降级!");
            throw new RuntimeException("进入降级!");

        }
        List<ToiletEntity> result = toiletDao.findAllByCleanAndAvailable(true, true);

        //抛出一个异常，使得进入降级
//        throw new RuntimeException("throw execption");
        return result.stream()
                .map(ToiletConverter::convert)
                .collect(Collectors.toList());
    }

    // 调用失败返回(降级熔断)
    public List<Toilet> getAvailableToiletFallback() {
        log.info("getAvailableToiletFallback - Fallback");
        return Lists.newArrayList();
    }


    @Override
    @PostMapping("/occpy")
    @SentinelResource(value = "occpy")
    public Toilet occpy(Long id) {

        ToiletEntity toilet = toiletDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Toilet not found"));

        if (!toilet.isAvailable() || !toilet.isClean()) {
            throw new RuntimeException("restroom not available or unclean");
        }

        toilet.setAvailable(false);
        toilet.setClean(false);
        toiletDao.save(toilet);
        return ToiletConverter.convert(toilet);
    }

    @Override
    @PostMapping("/release")
    public Toilet release(Long id) {
        ToiletEntity toilet = toiletDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Toilet not found"));

        toilet.setAvailable(true);
        toilet.setClean(true);
        toiletDao.save(toilet);
        return ToiletConverter.convert(toilet);

    }


    @Override
    @GetMapping("/checkAvailablility")
    public boolean checkAvailablility(Long id) throws InterruptedException {
        for (; id > 0; --id) {
            Thread.sleep(1000);
        }
        log.info("received");
        return true;
//        ToiletEntity toilet = toiletDao.findById(id)
//                .orElseThrow(() -> new RuntimeException("Toilet not found"));
//        return toilet.isAvailable();
    }

    @Override
    @GetMapping("/demo")
    public String demo() {
        log.info("test====demo");
        System.out.println("Restroom = " + "====ok11111");
        return "ok";
    }

    @Override
    @GetMapping("/test")
    public Toilet test(String input) {
        return Toilet.builder()
                .desc(input)
                .build();
    }

    @PostMapping("hystrixDemo")
    public void hystrixDemo(Long count) throws InterruptedException {
        Thread.sleep(count * 1000);
        log.info("restroom-hystrix");
    }

    @Override
    @PostMapping("/releaseTCC")
    // TCC方案
    @Transactional
    public Toilet releaseTcc(Long id) {
        try {
            log.info("*******  Try release  TCC *** , id {}", id);
            ToiletEntity toilet = toiletDao.findById(id)
                    .filter(e -> !e.isAvailable())
                    .orElseThrow(() -> new RuntimeException("Toilet not found"));

            toilet.setReserved(true);
            toiletDao.save(toilet);
            return ToiletConverter.convert(toilet);

        } catch (Exception e) {
            log.error("cannot release thr restroom", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean releaseCommit(BusinessActionContext actionContext) {
        try {
            Long id = Long.parseLong(actionContext.getActionContext("id").toString());
            log.info("*******  comfirm release  TCC ***  ,id {}, xid={}", id, actionContext.getXid());
            Optional<ToiletEntity> optionalToilet = toiletDao.findById(id);

            // 判断optionalToilet是否存在，和optionalToilet中是不是有isReserved对象
            if (optionalToilet.isPresent() && optionalToilet.get().isReserved()) {
                ToiletEntity entity = optionalToilet.get();
                entity.setClean(true);
                entity.setAvailable(true);
                entity.setReserved(false);
                toiletDao.save(entity);
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("cannot release thr restroom", e);
            return false;
        }
    }


    @Override
    @Transactional
    public boolean releaseCancel(BusinessActionContext actionContext) {
        try {
            Long id = Long.parseLong(actionContext.getActionContext("id").toString());
            log.info("*******  cancel release  TCC ***  ,id {}, xid={}", id, actionContext.getXid());
            Optional<ToiletEntity> toilet = toiletDao.findById(id);

            if (toilet.isPresent()) {
                ToiletEntity entity = toilet.get();
                entity.setClean(false);
                entity.setAvailable(false);
                entity.setReserved(false);
                toiletDao.save(entity);
            }
            return true;
        } catch (Exception e) {
            log.error("cannot release thr restroom", e);
            return false;
        }
    }

}
