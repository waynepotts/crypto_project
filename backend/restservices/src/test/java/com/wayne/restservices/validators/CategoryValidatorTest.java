package com.wayne.restservices.validators;

import com.wayne.restservices.entities.jpa.Category;
import com.wayne.restservices.exceptions.CategoryAlreadyExistsException;
import com.wayne.restservices.exceptions.CategoryNotFoundException;
import com.wayne.restservices.repositories.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryValidatorTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryValidator categoryValidator;

    @Test
    void shouldPassValidationWhenCategoryDoesNotExist() {
        when(categoryRepository.findByCoingeckoId("defi"))
                .thenReturn(null);

        assertDoesNotThrow(() ->
                categoryValidator.validateCategoryUnique("defi"));

        verify(categoryRepository)
                .findByCoingeckoId("defi");
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        when(categoryRepository.findByCoingeckoId("defi"))
                .thenReturn(new Category());

        CategoryAlreadyExistsException exception =
                assertThrows(
                        CategoryAlreadyExistsException.class,
                        () -> categoryValidator
                                .validateCategoryUnique("defi")
                );

        assertEquals(
                "Category already exists with Coingecko category ID: defi",
                exception.getMessage());

        verify(categoryRepository)
                .findByCoingeckoId("defi");
    }

    @Test
    void shouldPassExistenceCheckWhenCategoryExists() {
        when(categoryRepository.findByCoingeckoId("staking"))
                .thenReturn(new Category());

        assertDoesNotThrow(() ->
                categoryValidator.validateCategoryExists("staking"));

        verify(categoryRepository)
                .findByCoingeckoId("staking");
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.findByCoingeckoId("nft"))
                .thenReturn(null);

        CategoryNotFoundException exception =
                assertThrows(
                        CategoryNotFoundException.class,
                        () -> categoryValidator
                                .validateCategoryExists("nft")
                );

        assertEquals(
                "Category Not Found with Coingecko category ID: nft",
                exception.getMessage());

        verify(categoryRepository)
                .findByCoingeckoId("nft");
    }

    @Test
    void shouldThrowExceptionWhenNullCategoryIdPassed() {
        when(categoryRepository.findByCoingeckoId(null))
                .thenReturn(null);

        CategoryNotFoundException exception =
                assertThrows(
                        CategoryNotFoundException.class,
                        () -> categoryValidator.validateCategoryExists(null)
                );

        assertNotNull(exception.getMessage());

        verify(categoryRepository)
                .findByCoingeckoId(null);
    }

    @Test
    void shouldThrowExceptionWhenEmptyCategoryIdPassed() {
        when(categoryRepository.findByCoingeckoId(""))
                .thenReturn(null);

        CategoryNotFoundException exception =
                assertThrows(
                        CategoryNotFoundException.class,
                        () -> categoryValidator.validateCategoryExists("")
                );

        assertTrue(exception.getMessage().contains("Coingecko category ID: "));
    }
}
