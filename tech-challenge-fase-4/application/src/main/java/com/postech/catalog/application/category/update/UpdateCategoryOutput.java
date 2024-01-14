package com.postech.catalog.application.category.update;

import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryID;

public record UpdateCategoryOutput(
        CategoryID id
) {

    public static UpdateCategoryOutput from(final Category category) {
        return new UpdateCategoryOutput(category.getId());
    }
}
