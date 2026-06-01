package com.wayne.restservices.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String categoryId) {
        super("Category Not Found with Coingecko category ID: " + categoryId);
    }
}
