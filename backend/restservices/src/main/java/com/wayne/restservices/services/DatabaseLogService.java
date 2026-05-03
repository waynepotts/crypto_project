package com.wayne.restservices.services;

import com.wayne.restservices.entities.jpa.ApplicationLog;
import com.wayne.restservices.repositories.ApplicationLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DatabaseLogService {

    private final ApplicationLogRepository repository;

    public DatabaseLogService(
            ApplicationLogRepository repository) {

        this.repository = repository;
    }

    public void log(String level,
                    String logger,
                    String message) {

        ApplicationLog entry = new ApplicationLog();

        entry.setLevel(level);
        entry.setLogger(logger);
        entry.setMessage(message);
        entry.setCreatedAt(Instant.now());

        repository.save(entry);
    }
}