package com.xlf.employee.loadbalancer;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

//@Component
@Slf4j
public class ClusterFirstRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosProps;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        String cluster = nacosProps.getClusterName();
        String group = nacosProps.getGroup();

        BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        String serviceName = loadBalancer.getName();
        NamingService namingService = nacosServiceManager.getNamingService(nacosProps.getNacosProperties());
        try {
            List<Instance> instances = namingService.selectInstances(serviceName, group, true);
            if (StringUtils.isNotBlank(cluster)) {
                List<Instance> clusterInstances = instances.stream()
                        .filter(i -> StringUtils.equalsIgnoreCase(cluster, i.getClusterName()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(clusterInstances)){
                    instances = clusterInstances;
                    // 优先
                }
            }

            Instance instance = ExtendBalancer.getHostByRandomWeight2(instances);
            return new NacosServer(instance);

        } catch (NacosException e) {
            log.error("lb error", e);
        }
        return null;
    }
}
