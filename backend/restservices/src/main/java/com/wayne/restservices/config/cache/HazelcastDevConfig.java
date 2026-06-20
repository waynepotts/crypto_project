package com.wayne.restservices.config.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@Profile("dev")
@EnableCaching
public class HazelcastDevConfig {

    @Bean
    public Config hazelcastConfig() {

        Config config = new Config();

        config.setClusterName("crypto-dev");

        config.setInstanceName("dev-node-1");

        // Disable clustering (important for dev stability)
        config.getNetworkConfig()
                .getJoin()
                .getMulticastConfig()
                .setEnabled(false);

        config.getNetworkConfig()
                .getJoin()
                .getTcpIpConfig()
                .setEnabled(false);
        config.addMapConfig(new MapConfig(CacheNames.MARKET_DATA).setTimeToLiveSeconds((int) Duration.ofMinutes(5).toSeconds()).setInMemoryFormat(InMemoryFormat.BINARY));
        config.addMapConfig(new MapConfig(CacheNames.EXCHANGE_RATES).setTimeToLiveSeconds((int) Duration.ofMinutes(5).toSeconds()).setInMemoryFormat(InMemoryFormat.BINARY));
        config.addMapConfig(new MapConfig(CacheNames.COINS).setTimeToLiveSeconds((int) Duration.ofDays(1).toSeconds()).setInMemoryFormat(InMemoryFormat.BINARY));
        config.addMapConfig(new MapConfig(CacheNames.MARKET_CAP).setTimeToLiveSeconds((int) Duration.ofDays(1).toSeconds()).setInMemoryFormat(InMemoryFormat.BINARY));
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
