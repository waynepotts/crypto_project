package com.wayne.restservices.mappers;

import com.wayne.restservices.dtos.coingecko.CoinGeckoCategoryDto;
import com.wayne.restservices.entities.jpa.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    @Test
    void shouldMapCoinGeckoCategoryDtoToEntity() {
        CoinGeckoCategoryDto dto = new CoinGeckoCategoryDto("defi", "Decentralized Finance");

        Category category = CategoryMapper.toEntity(dto);

        assertNotNull(category);
        assertEquals("defi", category.getCoingeckoCategoryId());
        assertEquals("Decentralized Finance", category.getName());
        assertNull(category.getId());
    }

    @Test
    void shouldMapCategoryWithNullId() {
        CoinGeckoCategoryDto dto = new CoinGeckoCategoryDto(null, "Gaming");

        Category category = CategoryMapper.toEntity(dto);

        assertNotNull(category);
        assertNull(category.getCoingeckoCategoryId());
        assertEquals("Gaming", category.getName());
    }

    @Test
    void shouldMapCategoryWithEmptyName() {
        CoinGeckoCategoryDto dto = new CoinGeckoCategoryDto("gaming", "");

        Category category = CategoryMapper.toEntity(dto);

        assertNotNull(category);
        assertEquals("gaming", category.getCoingeckoCategoryId());
        assertEquals("", category.getName());
    }

    @Test
    void shouldMapComplexCategoryName() {
        CoinGeckoCategoryDto dto = new CoinGeckoCategoryDto("nft", "Non-Fungible Tokens");

        Category category = CategoryMapper.toEntity(dto);

        assertNotNull(category);
        assertEquals("nft", category.getCoingeckoCategoryId());
        assertEquals("Non-Fungible Tokens", category.getName());
    }
}
