package com.wayne.restservices.services;

import com.wayne.restservices.entities.jpa.Category;
import com.wayne.restservices.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    Category getCategory(String coingeckoId) {
        return categoryRepository.findByCoingeckoId(coingeckoId);
    }

    Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
}
