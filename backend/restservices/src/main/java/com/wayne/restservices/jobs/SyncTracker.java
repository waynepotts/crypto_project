package com.wayne.restservices.jobs;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SyncTracker {

    private final Set<String> activeSyncs =
            ConcurrentHashMap.newKeySet();

    public boolean start(String key) {
        return activeSyncs.add(key);
    }

    public void finish(String key) {
        activeSyncs.remove(key);
    }
}
