package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationLogRepository
        extends JpaRepository<ApplicationLog, Long> {
}