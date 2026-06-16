package com.wayne.restservices.config.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class HazelcastProdConfig {

    @Bean
    public Config hazelcastConfig() {

        Config config = new Config();

        config.setClusterName("crypto-prod");

        NetworkConfig network = config.getNetworkConfig();
        network.setPort(5701);
        network.setPortAutoIncrement(true);

        JoinConfig join = network.getJoin();

        join.getMulticastConfig().setEnabled(false);

        TcpIpConfig tcpIpConfig = join.getTcpIpConfig();
        tcpIpConfig.setEnabled(true);

        // Example cluster nodes (later Kubernetes replaces this)
        tcpIpConfig.addMember("10.0.0.1");
        tcpIpConfig.addMember("10.0.0.2");

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public CacheManager cacheManager(HazelcastInstance instance) {
        return new HazelcastCacheManager(instance);
    }
}
