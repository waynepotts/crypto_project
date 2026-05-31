package com.wayne.restservices.repositories;

import com.wayne.restservices.entities.jpa.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldFindByCoingeckoId() {
        String uniqueId = "test_category_" + System.nanoTime();
        Category category = new Category();
        category.setCoingeckoCategoryId(uniqueId);
        category.setName("Test Category");
        categoryRepository.save(category);

        Category found = categoryRepository.findByCoingeckoId(uniqueId);

        assertNotNull(found);
        assertEquals(uniqueId, found.getCoingeckoCategoryId());
        assertEquals("Test Category", found.getName());
    }

    @Test
    void shouldReturnNullWhenCoingeckoIdNotFound() {
        Category found = categoryRepository.findByCoingeckoId("nonexistent");

        assertNull(found);
    }

    @Test
    void shouldFindByName() {
        Category category = new Category();
        category.setCoingeckoCategoryId("gaming");
        category.setName("Gaming");
        categoryRepository.save(category);

        Category found = categoryRepository.findByName("Gaming");

        assertNotNull(found);
        assertEquals("Gaming", found.getName());
    }

    @Test
    void shouldFindByNameReturnsNullWhenNameNotFound() {
        Category found = categoryRepository.findByName("NonExistent");

        assertNull(found);
    }

    @Test
    void shouldSaveAndFindCategory() {
        Category category = new Category();
        category.setCoingeckoCategoryId("nft");
        category.setName("NFT");

        Category saved = categoryRepository.save(category);

        assertNotNull(saved.getId());
        Category found = categoryRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("nft", found.getCoingeckoCategoryId());
    }

    @Test
    void shouldDeleteCategory() {
        Category category = new Category();
        category.setCoingeckoCategoryId("layer1");
        category.setName("Layer 1");
        Category saved = categoryRepository.save(category);

        categoryRepository.deleteById(saved.getId());

        Category found = categoryRepository.findById(saved.getId()).orElse(null);
        assertNull(found);
    }
}
