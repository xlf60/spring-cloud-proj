package com.xlf.employee.loadbalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CustomeRule extends AbstractLoadBalancerRule {
     /*
     total = 0 // 当total==5以后，我们指针才能往下走，
     index = 0 // 当前对外提供服务的服务器地址，
     total需要重新置为零，但是已经达到过一个5次，我们的index = 1
     */

    private int total = 0;             // 总共被调用的次数，目前要求每台被调用5次
    private int currentIndex = 0;    // 当前提供服务的机器号

    public Server choose(ILoadBalancer lb, Object key) {
        log.info("--------Ribbon轮询策略Start-------");
        if (lb == null) {
            return null;
        }
        Server server = null;
        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers(); //当前存活的服务
            List<Server> allList = lb.getAllServers();  //获取全部的服务

            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }
            //int index = rand.nextInt(serverCount);
            //server = upList.get(index);
            if (total < 5) {
                server = upList.get(currentIndex);
                total++;
            } else {
                total = 0;
                currentIndex++;
                if (currentIndex >= upList.size()) {
                    currentIndex = 0;
                }
            }
            if (server == null) {
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }
            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }
        log.info("--------Ribbon轮询策略End-------");
        return server;
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }
}

