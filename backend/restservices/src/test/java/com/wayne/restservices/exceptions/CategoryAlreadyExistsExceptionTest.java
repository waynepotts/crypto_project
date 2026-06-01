package com.wayne.restservices.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryAlreadyExistsExceptionTest {

    @Test
    void shouldContainCategoryIdInMessage() {
        CategoryAlreadyExistsException ex = new CategoryAlreadyExistsException("defi");

        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("defi"));
        assertTrue(ex.getMessage().contains("Category already exists"));
    }

    @Test
    void shouldContainCorrectPrefixInMessage() {
        CategoryAlreadyExistsException ex = new CategoryAlreadyExistsException("staking");

        assertTrue(ex.getMessage().startsWith("Category already exists with Coingecko category ID: "));
    }

    @Test
    void shouldHaveRightExceptionType() {
        CategoryAlreadyExistsException ex = new CategoryAlreadyExistsException("test");

        assertTrue(ex instanceof RuntimeException);
        assertTrue(ex instanceof CategoryAlreadyExistsException);
    }

    @Test
    void shouldHandleEmptyCategoryIdInMessage() {
        CategoryAlreadyExistsException ex = new CategoryAlreadyExistsException("");

        assertTrue(ex.getMessage().contains("Coingecko category ID: "));
    }
}
