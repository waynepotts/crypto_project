# AGENTS.md

## Project Overview

This project is a cryptocurrency data platform that ingests market and reference data from CoinGecko and exposes it through a REST API for consumption by a React frontend.

The backend is implemented using:

* Java 21
* Spring Boot 4
* Spring Data JPA
* PostgreSQL
* Liquibase
* Maven

The primary goals are:

* Clean architecture
* Maintainability
* Testability
* Long-term stability
* AI-assisted development compatibility

---

# Architecture Principles

## DTO First

Controllers must never expose JPA entities directly.

Use:

```text
Controller
  -> Service
      -> Repository
          -> Entity
```

and

```text
Entity
  -> Mapper
      -> DTO
```

All public API contracts must use DTOs.

---

## Validation

Business validation belongs in validators or services.

Do not place business rules inside controllers.

Examples:

* Coin already exists
* Invalid CoinGecko ID
* Invalid date range

Use custom exceptions where appropriate.

---

## Exception Handling

All exceptions should be handled through:

```java
@RestControllerAdvice
```

Never return raw stack traces.

Preferred pattern:

```java
CoinNotFoundException
ValidationException
DuplicateCoinException
ExternalServiceException
```

mapped to:

```java
ErrorResponseDto
```

---

## Database Management

### IMPORTANT

Never modify the database schema manually.

All schema changes must be implemented through Liquibase change sets.

Use:

```text
src/main/resources/db/changelog
```

for all schema modifications.

---

## Liquibase Rules

Do not modify historical change sets that have already been executed.

Create a new change set instead.

Example:

```text
001-initial-schema.yaml
002-market-data.yaml
003-add-index.yaml
004-add-column.yaml
```

not:

```text
Modify 002-market-data.yaml
```

after it has been applied.

---

# Entity Design

## JPA Entities

Entities represent database tables only.

Avoid business logic inside entities.

Use:

```java
@Entity
@Table(...)
```

Entities should be mutable.

Do not use records for JPA entities.

---

## DTOs

DTOs should use Java records whenever possible.

Example:

```java
public record CoinResponseDto(
        Long id,
        String name,
        String symbol
) {}
```

Use records for:

* Request DTOs
* Response DTOs
* CoinGecko integration DTOs

---

# Repository Guidelines

Repositories should remain thin.

Prefer Spring Data derived queries.

Example:

```java
findByCoinId(...)
findByCoinAndLastUpdated(...)
```

Use JPQL only when necessary.

Use native SQL only when JPQL cannot solve the problem.

---

# Service Layer Guidelines

Business logic belongs in services.

Services should:

* validate inputs
* coordinate repositories
* call external APIs
* perform mappings

Services should not perform direct HTTP response handling.

---

# CoinGecko Integration

CoinGecko DTOs must be separated from public API DTOs.

Preferred flow:

```text
CoinGecko API
    ↓
CoinGecko DTO
    ↓
Mapper
    ↓
Entity
    ↓
Application DTO
```

Never expose CoinGecko DTOs directly from REST controllers.

---

# Historical Market Data

Historical data should be stored at multiple granularities.

Supported granularities:

```java
ChronoUnit.MINUTES
ChronoUnit.HOURS
ChronoUnit.DAYS
```

Store the granularity explicitly.

Do not infer granularity from timestamps.

---

# Pagination

Endpoints returning collections should use pagination.

Preferred pattern:

```java
Page<T>
```

with:

```java
Pageable pageable
```

Default ordering:

```java
lastUpdated DESC
```

for time-series data.

---

# Testing Standards

## Unit Tests

Use:

* JUnit 5
* Mockito

Mock repositories and external services.

Do not connect to real databases.

---

## Integration Tests

Use:

```java
@SpringBootTest
@AutoConfigureMockMvc
```

for REST integration testing.

Verify:

* status codes
* JSON responses
* exception handling

---

## Naming Convention

Use:

```java
shouldCreateCoin()
shouldUpdateCoin()
shouldThrowWhenCoinNotFound()
```

Avoid generic names such as:

```java
test1()
testCoin()
```

---

# Logging

Use SLF4J.

Example:

```java
private static final Logger LOGGER =
        LoggerFactory.getLogger(MyClass.class);
```

Do not use:

```java
System.out.println()
```

---

# API Documentation

OpenAPI documentation is generated through springdoc.

Every public endpoint should include:

* summary
* description
* response codes

Swagger UI should remain functional.

---

# Coding Standards

## Prefer

* constructor injection
* records for DTOs
* BigDecimal for monetary values
* Instant for timestamps
* Set for many-to-many relationships

## Avoid

* field injection
* floating point types for financial data
* exposing entities through controllers
* EAGER fetching

---

# Environment Variables

Configuration must come from environment variables.

Never commit:

* API keys
* passwords
* secrets

Use:

```text
.env.dev
.env.test
.env.prod
```

---

# AI Agent Instructions

When generating code:

1. Follow existing project structure.
2. Use Java 21 features where appropriate.
3. Prefer records for DTOs.
4. Prefer constructor injection.
5. Use Liquibase for schema changes.
6. Create tests for new functionality.
7. Do not expose entities from REST controllers.
8. Use BigDecimal for financial values.
9. Preserve backward compatibility where practical.
10. Prioritize maintainability over cleverness.

```
```
