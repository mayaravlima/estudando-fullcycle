package com.postech.catalog.infrastructure.category.presenters;

import com.postech.catalog.application.category.retrieve.get.CategoryOutput;
import com.postech.catalog.application.category.retrieve.list.CategoryListOutput;
import com.postech.catalog.infrastructure.category.models.CategoryListResponse;
import com.postech.catalog.infrastructure.category.models.CategoryResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
