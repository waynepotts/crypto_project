package com.wayne.restservices.services;

import com.wayne.restservices.entities.jpa.ApplicationLog;
import com.wayne.restservices.repositories.ApplicationLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseLogServiceTest {

    @Mock
    private ApplicationLogRepository repository;

    @InjectMocks
    private DatabaseLogService databaseLogService;

    @Captor
    private ArgumentCaptor<ApplicationLog> logCaptor;

    @Test
    void shouldLogMessage() {
        databaseLogService.log("INFO", "TestLogger", "Test message");

        verify(repository).save(logCaptor.capture());
        ApplicationLog saved = logCaptor.getValue();

        assertEquals("INFO", saved.getLevel());
        assertEquals("TestLogger", saved.getLogger());
        assertEquals("Test message", saved.getMessage());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void shouldLogErrorMessage() {
        databaseLogService.log("ERROR", "ErrorLogger", "Something went wrong");

        verify(repository).save(logCaptor.capture());
        ApplicationLog saved = logCaptor.getValue();

        assertEquals("ERROR", saved.getLevel());
        assertEquals("ErrorLogger", saved.getLogger());
        assertEquals("Something went wrong", saved.getMessage());
    }
}
