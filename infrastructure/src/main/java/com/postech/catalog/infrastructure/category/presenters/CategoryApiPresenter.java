package com.postech.catalog.infrastructure.category.presenters;

import com.postech.catalog.application.category.retrieve.get.CategoryOutput;
import com.postech.catalog.application.category.retrieve.list.CategoryListOutput;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.infrastructure.category.models.CategoryListResponse;
import com.postech.catalog.infrastructure.category.models.CategoryResponse;

import java.util.List;
import java.util.stream.Collectors;

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
    public static Pagination<CategoryListResponse> present(Pagination<CategoryListOutput> categoryListOutputPagination) {
        List<CategoryListOutput> content = categoryListOutputPagination.items();
        List<CategoryListResponse> categoryListResponses = content.stream()
                .map(CategoryApiPresenter::convertToResponse)
                .collect(Collectors.toList());

        return new Pagination<>(
                categoryListOutputPagination.currentPage(),
                categoryListOutputPagination.perPage(),
                categoryListOutputPagination.total(),
                categoryListResponses
        );
    }

    private static CategoryListResponse convertToResponse(CategoryListOutput categoryListOutput) {
        return new CategoryListResponse(
                categoryListOutput.id().getValue(),
                categoryListOutput.name(),
                categoryListOutput.description(),
                categoryListOutput.isActive(),
                categoryListOutput.createdAt(),
                categoryListOutput.deletedAt()
        );
    }

}
