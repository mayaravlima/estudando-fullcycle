package com.postech.catalog.domain.catagory;

import com.postech.catalog.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);

    void deleteById(CategoryID id);

    Optional<Category> findById(CategoryID id);

    Category update(Category category);

    Pagination<Category> findAll(CategorySearchQuery query);

    List<CategoryID> existsByIds(Iterable<CategoryID> ids);

    void deleteCategoryFromVideoCategory(CategoryID categoryId);
}
