package com.postech.catalog.application.category.retrieve.list;

import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryID;

import java.time.Instant;

public record CategoryListOutput(
        CategoryID id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {
    public static CategoryListOutput from(Category category) {
        return new CategoryListOutput(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getIsActive(),
                category.getCreatedAt(),
                category.getDeletedAt()
        );
    }
}
