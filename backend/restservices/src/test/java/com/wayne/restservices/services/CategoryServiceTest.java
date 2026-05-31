package com.wayne.restservices.services;

import com.wayne.restservices.entities.jpa.Category;
import com.wayne.restservices.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldGetCategoryByCoingeckoId() {
        String coingeckoId = "defi";
        Category category = new Category();
        category.setCoingeckoCategoryId(coingeckoId);
        category.setName("DeFi");

        when(categoryRepository.findByCoingeckoId(coingeckoId)).thenReturn(category);

        Category result = categoryService.getCategory(coingeckoId);

        assertNotNull(result);
        assertEquals(coingeckoId, result.getCoingeckoCategoryId());
        assertEquals("DeFi", result.getName());
        verify(categoryRepository).findByCoingeckoId(coingeckoId);
    }

    @Test
    void shouldCreateCategory() {
        Category category = new Category();
        category.setCoingeckoCategoryId("gaming");
        category.setName("Gaming");

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals("gaming", result.getCoingeckoCategoryId());
        assertEquals("Gaming", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldUpdateCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setCoingeckoCategoryId("defi");
        category.setName("DeFi Updated");

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.updateCategory(category);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("defi", result.getCoingeckoCategoryId());
        assertEquals("DeFi Updated", result.getName());
        verify(categoryRepository).save(category);
    }
}
