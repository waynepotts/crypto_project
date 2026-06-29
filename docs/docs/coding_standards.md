# Coding Standards

## Purpose

Consistent coding standards improve readability, reduce the cognitive load required to understand unfamiliar code, and make it easier for multiple developers to collaborate. These standards apply to all code committed to this repository.

---

# General Principles

* Write code that is easy to understand rather than clever.
* Prefer simplicity over unnecessary abstraction.
* Keep methods and functions small and focused on a single responsibility.
* Avoid duplicated code.
* Write code that is self-documenting through meaningful names.
* Favor composition over inheritance where practical.
* Ensure all new functionality is accompanied by appropriate unit tests.

---

# Language

All identifiers, comments, documentation, commit messages, and log messages **must be written in English**.

The project uses **American English spelling** throughout.

Examples:

| Preferred  | Avoid      |
| ---------- | ---------- |
| initialize | initialise |
| color      | colour     |
| optimize   | optimise   |
| canceled   | cancelled  |
| behavior   | behaviour  |

Using a single language and spelling convention keeps the codebase consistent and improves searchability.

---

# Naming Conventions

## Java

Java code should follow standard Java naming conventions.

| Element    | Convention       | Example                           |
| ---------- | ---------------- | --------------------------------- |
| Classes    | PascalCase       | `CoinService`                     |
| Interfaces | PascalCase       | `MarketDataRepository`            |
| Records    | PascalCase       | `CoinDto`                         |
| Methods    | camelCase        | `findCoinById()`                  |
| Variables  | camelCase        | `marketData`                      |
| Parameters | camelCase        | `coinId`                          |
| Fields     | camelCase        | `exchangeRate`                    |
| Constants  | UPPER_SNAKE_CASE | `DEFAULT_PAGE_SIZE`               |
| Packages   | lowercase        | `com.wayne.restservices.services` |

---

## TypeScript

TypeScript should follow the same conventions wherever possible.

| Element          | Convention                     |
| ---------------- | ------------------------------ |
| Classes          | PascalCase                     |
| Interfaces       | PascalCase                     |
| Types            | PascalCase                     |
| Enums            | PascalCase                     |
| Functions        | camelCase                      |
| Variables        | camelCase                      |
| React Components | PascalCase                     |
| Hooks            | camelCase beginning with `use` |

Examples:

```typescript
const exchangeRate = 1.27;

function calculatePrice() {}

interface CoinDto {}

type ChartData = {};
```

Using the same conventions in both Java and TypeScript reduces context switching when moving between the backend and frontend.

---

# Translation Keys

Translation keys should use:

* lowercase letters
* snake_case
* American English spelling

Examples:

```text
price_history
display_currency
refresh_prices_now
no_currencies_found
update_frequency
```

Avoid:

```text
PriceHistory
Price_History
priceHistory
displayCurrency
Display_Currency
```

Translation keys represent identifiers rather than displayed text and should therefore remain stable even if the English wording changes.

---

# Formatting

## Braces

Use **K&R (Kernighan and Ritchie)** brace style.

Example:

```java
public Coin getCoin(Long id) {
    if (id == null) {
        return null;
    }

    return repository.findById(id)
            .orElseThrow();
}
```

Do not use Allman style.

Incorrect:

```java
public Coin getCoin(Long id)
{
    ...
}
```

---

## Indentation

* Use four spaces for indentation.
* Do not use tabs.
* Configure your IDE to insert spaces instead of tabs.

---

## Line Length

Aim for a maximum line length of approximately **120 characters**.

Long method calls should be wrapped naturally.

Example:

```java
CoinHistoryResponseDto response =
        marketDataService.getCoinHistory(
                coinId,
                from,
                to,
                granularity);
```

---

# Comments

Good code should require relatively few comments.

Comments should explain **why**, not **what**.

Good:

```java
// CoinGecko limits requests to 30 days for this endpoint.
```

Poor:

```java
// Increment i
i++;
```

Remove obsolete comments when modifying code.

---

# Logging

Use parameterized logging.

Correct:

```java
log.info("Loaded {} coins", count);
```

Avoid:

```java
log.info("Loaded " + count + " coins");
```

Do not log passwords, API keys, or other sensitive information.

---

# Null Handling

Avoid returning `null` where practical.

Prefer:

* `Optional<T>` for repository methods
* Empty collections instead of `null`
* Immutable DTOs using Java records

---

# Dependency Injection

Use constructor injection exclusively.

Avoid field injection with `@Autowired`.

Correct:

```java
public CoinService(CoinRepository repository) {
    this.repository = repository;
}
```

---

# DTOs

* DTOs should be immutable.
* Prefer Java records.
* Do not expose JPA entities through REST APIs.
* Perform mapping between entities and DTOs using dedicated mapper classes.

---

# Exceptions

Use meaningful custom exceptions.

Examples:

```
CoinNotFoundException
CategoryNotFoundException
ExchangeRateUnavailableException
```

Do not throw generic `Exception` or `RuntimeException` unless there is a compelling reason.

---

# Testing

Every new feature should include appropriate tests.

Tests should follow the Arrange–Act–Assert pattern.

Method names should describe the expected behavior.

Examples:

```
shouldReturnCoinWhenIdExists()

shouldThrowExceptionWhenCoinDoesNotExist()

shouldCacheMarketData()
```

---

# Dependencies

Before introducing a new dependency:

* Verify that similar functionality does not already exist in the project.
* Prefer mature, well-maintained libraries.
* Keep external dependencies to a minimum.
* Remove unused dependencies promptly.

---

# AI-Assisted Development

AI coding assistants are encouraged to improve developer productivity.

However:

* All AI-generated code must be reviewed before committing.
* AI-generated code must comply with these coding standards.
* Developers remain responsible for the correctness, security, performance, and maintainability of all committed code.
* AI should assist development, not replace engineering judgment.

---

# Consistency

Consistency is more valuable than personal preference.

When extending existing code, follow the conventions already established in that area of the codebase unless there is a clear architectural reason to refactor. A consistent codebase is easier to maintain, easier to review, and easier for new contributors to understand.
