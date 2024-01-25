package com.postech.catalog.application.category.update;

import com.postech.catalog.IntegrationTest;
import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.exceptions.DomainException;
import com.postech.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.postech.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var category =
                Category.newCategory("Film", null, true);

        save(category);

        final var expectedName = "Films";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(1, categoryRepository.count());

        final var actualOutput = useCase.execute(command).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var category =
                Category.newCategory("Film", null, true);

        save(category);

        final String expectedName = null;
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;
        final var expectedId = category.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var category =
                Category.newCategory("Film", null, true);

        save(category);

        final var expectedName = "Films";
        final var expectedDescription = "Category description";
        final var expectedIsActive = false;
        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertTrue(category.getIsActive());
        assertNull(category.getDeletedAt());

        final var actualOutput = useCase.execute(command).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var category =
                Category.newCategory("Film", null, true);

        save(category);

        final var expectedName = "Films";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;
        final var expectedId = category.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(category.getName(), actualCategory.getName());
        assertEquals(category.getDescription(), actualCategory.getDescription());
        assertEquals(category.getIsActive(), actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Films";
        final var expectedDescription = "Category description";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var command = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException =
                assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... category) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(category)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }
}
