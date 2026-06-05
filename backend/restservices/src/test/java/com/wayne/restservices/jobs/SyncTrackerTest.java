package com.wayne.restservices.jobs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SyncTrackerTest {

    private final SyncTracker syncTracker = new SyncTracker();

    @Test
    void shouldStartNewSync() {
        assertThat(syncTracker.start("test-key")).isTrue();
    }

    @Test
    void shouldNotStartDuplicateSync() {
        syncTracker.start("test-key");
        assertThat(syncTracker.start("test-key")).isFalse();
    }

    @Test
    void shouldAllowDifferentKeysSimultaneously() {
        assertThat(syncTracker.start("key-1")).isTrue();
        assertThat(syncTracker.start("key-2")).isTrue();
    }

    @Test
    void shouldFinishSyncAndAllowRestart() {
        syncTracker.start("test-key");
        syncTracker.finish("test-key");
        assertThat(syncTracker.start("test-key")).isTrue();
    }

    @Test
    void shouldAllowMultipleDifferentKeysAfterFinish() {
        syncTracker.start("key-1");
        syncTracker.start("key-2");
        syncTracker.finish("key-1");
        assertThat(syncTracker.start("key-3")).isTrue();
        assertThat(syncTracker.start("key-1")).isTrue();
    }
}
