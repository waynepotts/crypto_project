package com.wayne.restservices.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String categoryId) {
        super("Category already exists with Coingecko category ID: " + categoryId);
    }
}
