package com.postech.catalog.application.category.create;

import com.postech.catalog.IntegrationTest;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCategoryUseCaseIT {
    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Films";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;

        assertEquals(0, categoryRepository.count());

        final var command =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = useCase.execute(command).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        assertEquals(1, categoryRepository.count());

        final var actualCategory =
                categoryRepository.findById(actualOutput.id().getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        assertEquals(0, categoryRepository.count());

        final var command =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        assertEquals(0, categoryRepository.count());

        verify(categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Films";
        final var expectedDescription = "Category description";
        final var expectedIsActive = false;

        assertEquals(0, categoryRepository.count());

        final var command =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = useCase.execute(command).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        assertEquals(1, categoryRepository.count());

        final var actualCategory =
                categoryRepository.findById(actualOutput.id().getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = "Films";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var command =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).create(any());

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());
    }
}
