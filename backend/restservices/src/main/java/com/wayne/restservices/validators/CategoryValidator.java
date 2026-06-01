package com.wayne.restservices.validators;

import com.wayne.restservices.exceptions.CategoryAlreadyExistsException;
import com.wayne.restservices.exceptions.CategoryNotFoundException;
import com.wayne.restservices.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public CategoryValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public void validateCategoryUnique(
            String categoryId
    ) {
        if (categoryRepository.findByCoingeckoId(categoryId) != null) {
            throw new CategoryAlreadyExistsException(categoryId);
        }
    }

    public void validateCategoryExists(String categoryId){
        if (categoryRepository.findByCoingeckoId(categoryId) == null) {
            throw new CategoryNotFoundException(categoryId);
        }
    }
}
