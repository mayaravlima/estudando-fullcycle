package com.postech.catalog.application.category.retrieve.list;

import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategorySearchQuery;
import com.postech.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        final var categories = List.of(
                Category.newCategory("Films", "Description films", true),
                Category.newCategory("Cartoons", "Description cartoons", true)

        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var expectedPagination = new Pagination<Category>(
                expectedPage,
                expectedPerPage,
                categories.size(),
                categories
        );

        final var expectedItemsCount = 2;
        final var expectedResults = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResults = useCase.execute(query);

        assertEquals(expectedItemsCount, actualResults.items().size());
        assertEquals(expectedResults, actualResults);
        assertEquals(expectedPage, actualResults.currentPage());
        assertEquals(expectedPerPage, actualResults.perPage());
        assertEquals(categories.size(), actualResults.total());

    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldEmptyCategories() {
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var query = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var expectedPagination = new Pagination<Category>(
                expectedPage,
                expectedPerPage,
                0,
                categories
        );

        final var expectedItemsCount = 0;
        final var expectedResults = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(query)))
                .thenReturn(expectedPagination);

        final var actualResults = useCase.execute(query);

        assertEquals(expectedItemsCount, actualResults.items().size());
        assertEquals(expectedResults, actualResults);
        assertEquals(expectedPage, actualResults.currentPage());
        assertEquals(expectedPerPage, actualResults.perPage());
        assertEquals(0, actualResults.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var query = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        when(categoryGateway.findAll(eq(query)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(query)
        );

        assertEquals(expectedErrorMessage, actualException.getMessage());

    }
}
