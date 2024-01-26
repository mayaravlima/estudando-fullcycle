package com.postech.catalog.application.category.retrieve.list;

import com.postech.catalog.application.UseCase;
import com.postech.catalog.domain.catagory.CategorySearchQuery;
import com.postech.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
