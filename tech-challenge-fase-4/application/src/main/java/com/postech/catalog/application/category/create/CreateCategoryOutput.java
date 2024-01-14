package com.postech.catalog.application.category.create;

import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {
    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }
}
