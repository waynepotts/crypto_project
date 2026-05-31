package com.wayne.restservices.mappers;

import com.wayne.restservices.dtos.coingecko.CoinGeckoCategoryDto;
import com.wayne.restservices.entities.jpa.Category;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;

public class CategoryMapper {
    public static @NonNull Category toEntity(@NotNull CoinGeckoCategoryDto dto){
        Category category = new Category();
        category.setCoingeckoCategoryId(dto.categoryId());
        category.setName(dto.name());
        return category;
    }
}
