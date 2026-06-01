package com.wayne.restservices.services;

import com.wayne.restservices.entities.jpa.Category;
import com.wayne.restservices.repositories.CategoryRepository;
import com.wayne.restservices.validators.CategoryValidator;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
    public CategoryService(CategoryRepository categoryRepository, CategoryValidator categoryValidator) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    Category getCategory(String categoryId) {
        categoryValidator.validateCategoryExists(categoryId);
        return categoryRepository.findByCoingeckoId(categoryId);
    }

    Category createCategory(Category category) {
        categoryValidator.validateCategoryUnique(category.getCoingeckoCategoryId());
        return categoryRepository.save(category);
    }

    Category updateCategory(Category category) {
        categoryValidator.validateCategoryExists(category.getCoingeckoCategoryId());
        return categoryRepository.save(category);
    }
}
