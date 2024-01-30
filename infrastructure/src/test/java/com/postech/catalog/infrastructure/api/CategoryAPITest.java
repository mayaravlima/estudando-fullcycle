package com.postech.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.catalog.ControllerTest;
import com.postech.catalog.application.category.create.CreateCategoryOutput;
import com.postech.catalog.application.category.create.CreateCategoryUseCase;
import com.postech.catalog.application.category.delete.DeleteCategoryUseCase;
import com.postech.catalog.application.category.retrieve.get.CategoryOutput;
import com.postech.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.postech.catalog.application.category.retrieve.list.CategoryListOutput;
import com.postech.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.postech.catalog.application.category.update.UpdateCategoryOutput;
import com.postech.catalog.application.category.update.UpdateCategoryUseCase;
import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.DomainException;
import com.postech.catalog.domain.exceptions.NotFoundException;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.validation.Error;
import com.postech.catalog.domain.validation.handler.Notification;
import com.postech.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.postech.catalog.infrastructure.category.models.UpdateCategoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {
    @Autowired
    private WebTestClient mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var input =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from("123")));

        final var request = this.mvc.post().uri(uriBuilder -> uriBuilder.path("/categories").build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(this.mapper.writeValueAsString(input))
                .exchange().expectStatus().isCreated().expectBody().jsonPath("$.id").isEqualTo("123");

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var input =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = this.mvc.post().uri(uriBuilder -> uriBuilder.path("/categories").build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(this.mapper.writeValueAsString(input))
                .exchange().expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody().jsonPath("$.errors[0].message").isEqualTo(expectedMessage);

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var input =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = this.mvc.post().uri(uriBuilder -> uriBuilder.path("/categories").build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(this.mapper.writeValueAsString(input))
                .exchange().expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody().jsonPath("$.message").isEqualTo(expectedMessage)
                .jsonPath("$.errors[0].message").isEqualTo(expectedMessage);


        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var category =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = category.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(category));

        final var request = this.mvc.get().uri(uriBuilder -> uriBuilder.path("/categories/{id}").build(expectedId))
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.id").isEqualTo(expectedId)
                .jsonPath("$.name").isEqualTo(expectedName)
                .jsonPath("$.description").isEqualTo(expectedDescription)
                .jsonPath("$.is_active").isEqualTo(expectedIsActive)
                .jsonPath("$.created_at").isEqualTo(category.getCreatedAt().toString())
                .jsonPath("$.updated_at").isEqualTo(category.getUpdatedAt().toString())
                .jsonPath("$.deleted_at").isEqualTo(category.getDeletedAt());

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        final var request = this.mvc.get().uri(uriBuilder -> uriBuilder.path("/categories/{id}").build(expectedId.getValue()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isNotFound()
                .expectBody().jsonPath("$.message").isEqualTo(expectedErrorMessage);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Movies";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final var command =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = this.mvc.put().uri(uriBuilder -> uriBuilder.path("/categories/{id}").build(expectedId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(command)).exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.id", equalTo(expectedId));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be null";

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var command =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = this.mvc.put().uri(uriBuilder -> uriBuilder.path("/categories/{id}").build(expectedId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(command))
                .exchange().expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody().jsonPath("$.errors[0].message").isEqualTo(expectedMessage)
                .jsonPath("$.errors", hasSize(expectedErrorCount));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    //
    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() throws Exception {
        final var expectedId = "not-found";
        final var expectedName = "Movies";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID not-found was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var command =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = this.mvc.put().uri(uriBuilder -> uriBuilder.path("/categories/{id}").build(expectedId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(command)).exchange().expectStatus()
                .isNotFound().expectBody().jsonPath("$.message").isEqualTo(expectedErrorMessage);


        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldReturnNoContent() throws Exception {
        final var expectedId = "123";

        doNothing()
                .when(deleteCategoryUseCase).execute(any());

        final var request = this.mvc.delete().uri(
                uriBuilder -> uriBuilder.path("/categories").path("/{id}").build(expectedId)
        ).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNoContent();

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {
        final var category = Category.newCategory("Movies", null, true);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CategoryListOutput.from(category));

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = this.mvc.get().uri(uriBuilder ->
                        uriBuilder.path("/categories").queryParam("page", String.valueOf(expectedPage))
                                .queryParam("perPage", String.valueOf(expectedPerPage))
                                .queryParam("sort", expectedSort)
                                .queryParam("dir", expectedDirection)
                                .queryParam("search", expectedTerms).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].current_page").isEqualTo(expectedPage)
                .jsonPath("$[0].per_page").isEqualTo(expectedPerPage)
                .jsonPath("$[0].total").isEqualTo(expectedTotal)
                .jsonPath("$[0].items[0].id").isEqualTo(category.getId().getValue())
                .jsonPath("$[0].items[0].name").isEqualTo(category.getName())
                .jsonPath("$[0].items[0].description").isEqualTo(category.getDescription())
                .jsonPath("$[0].items[0].is_active").isEqualTo(category.getIsActive())
                .jsonPath("$[0].items[0].created_at").isEqualTo(category.getCreatedAt().toString())
                .jsonPath("$[0].items[0].deleted_at").isEqualTo(category.getDeletedAt());

        verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
