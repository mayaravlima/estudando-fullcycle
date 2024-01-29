package com.postech.catalog.infrastructure.api.controllers;

import com.postech.catalog.application.category.create.CreateCategoryCommand;
import com.postech.catalog.application.category.create.CreateCategoryOutput;
import com.postech.catalog.application.category.create.CreateCategoryUseCase;
import com.postech.catalog.application.category.delete.DeleteCategoryUseCase;
import com.postech.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.postech.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.postech.catalog.application.category.update.UpdateCategoryCommand;
import com.postech.catalog.application.category.update.UpdateCategoryOutput;
import com.postech.catalog.application.category.update.UpdateCategoryUseCase;
import com.postech.catalog.domain.catagory.CategorySearchQuery;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.validation.handler.Notification;
import com.postech.catalog.infrastructure.api.CategoryAPI;
import com.postech.catalog.infrastructure.category.models.CategoryListResponse;
import com.postech.catalog.infrastructure.category.models.CategoryResponse;
import com.postech.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.postech.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.postech.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase
    ) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    public Mono<ResponseEntity<?>> createCategory(final CreateCategoryRequest input) {
        final var command = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return Mono.just(this.createCategoryUseCase.execute(command)
                .fold(onError, onSuccess));
    }

    @Override
    public Flux<Pagination<CategoryListResponse>> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return Flux.defer(() ->
                Mono.fromCallable(() ->
                                listCategoriesUseCase.execute(new CategorySearchQuery(page, perPage, search, sort, direction))
                        )
                        .map(CategoryApiPresenter::present)
                        .flatMapMany(Flux::just)
        );
    }

    @Override
    public Mono<CategoryResponse> getById(final String id) {
        return(Mono.just(CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id))));
    }

    @Override
    public Mono<ResponseEntity<?>> updateById(final String id, final UpdateCategoryRequest input) {
        final var command = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return Mono.just(this.updateCategoryUseCase.execute(command)
                .fold(onError, onSuccess));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}
