package com.xlf.restroom.util;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

public class SentinelRule {

    /**
     * 通过代码定义流量控制规则
     */
    public static void initFlowRules(String resourceName) {
        List<FlowRule> rules = new ArrayList<>();
        //定义一个限流规则对象
        FlowRule rule = new FlowRule();
        //资源名称
        rule.setResource(resourceName);
        //限流阈值的类型
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 设置 QPS 的阈值为 2
        rule.setCount(2);
        rules.add(rule);
        //定义限流规则
        FlowRuleManager.loadRules(rules);
    }

    /**
     * 初始化熔断策略
     */
    public static void initDegradeRule(String resourceName) {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule(resourceName);
        //熔断策略为异常比例
        rule.setGrade(CircuitBreakerStrategy.ERROR_RATIO.getType());
        //异常比例阈值
        rule.setCount(0.7);
        //最小请求数
        rule.setMinRequestAmount(100);
        //统计市场，单位毫秒
        rule.setStatIntervalMs(30000);
        //熔断市场，单位秒
        rule.setTimeWindow(5);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }

}
