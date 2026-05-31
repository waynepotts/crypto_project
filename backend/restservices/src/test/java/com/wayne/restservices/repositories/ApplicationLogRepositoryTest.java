package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.ApplicationLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ApplicationLogRepositoryTest {

    @Autowired
    private ApplicationLogRepository applicationLogRepository;

    @Test
    void shouldSaveApplicationLog() {
        ApplicationLog log = new ApplicationLog();
        log.setLevel("INFO");
        log.setLogger("test");
        log.setMessage("Test log message");
        log.setCreatedAt(Instant.now());

        ApplicationLog saved = applicationLogRepository.save(log);

        assertNotNull(saved.getId());
        assertEquals("INFO", saved.getLevel());
        assertEquals("Test log message", saved.getMessage());
    }

    @Test
    void shouldFindApplicationLogById() {
        ApplicationLog log = new ApplicationLog();
        log.setLevel("ERROR");
        log.setLogger("test");
        log.setMessage("Test error");
        log.setCreatedAt(Instant.now());
        ApplicationLog saved = applicationLogRepository.save(log);

        ApplicationLog found = applicationLogRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals("ERROR", found.getLevel());
    }

    @Test
    void shouldReturnEmptyWhenLogIdNotFound() {
        ApplicationLog found = applicationLogRepository.findById(Long.MAX_VALUE).orElse(null);
        assertNull(found);
    }
}
