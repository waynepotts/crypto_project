package com.wayne.restservices.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryNotFoundExceptionTest {

    @Test
    void shouldContainCategoryIdInMessage() {
        CategoryNotFoundException ex = new CategoryNotFoundException("defi");

        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("defi"));
        assertTrue(ex.getMessage().contains("Category Not Found"));
    }

    @Test
    void shouldContainCorrectPrefixInMessage() {
        CategoryNotFoundException ex = new CategoryNotFoundException("staking");

        assertTrue(ex.getMessage().startsWith("Category Not Found with Coingecko category ID: "));
    }

    @Test
    void shouldHaveRightExceptionType() {
        CategoryNotFoundException ex = new CategoryNotFoundException("test");

        assertTrue(ex instanceof RuntimeException);
        assertTrue(ex instanceof CategoryNotFoundException);
    }

    @Test
    void shouldHandleEmptyCategoryIdInMessage() {
        CategoryNotFoundException ex = new CategoryNotFoundException("");

        assertTrue(ex.getMessage().contains("Coingecko category ID: "));
    }
}
