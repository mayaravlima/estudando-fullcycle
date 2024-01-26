package com.postech.catalog.application.category.create;

import com.postech.catalog.domain.catagory.Category;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId().getValue());
    }

    public static CreateCategoryOutput from(final String id) {
        return new CreateCategoryOutput(id);
    }
}
