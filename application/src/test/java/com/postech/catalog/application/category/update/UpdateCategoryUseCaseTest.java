package com.postech.catalog.application.category.update;

import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var category = Category.newCategory("Film", null, true);

        final var expectedName = "Movie";
        final var expectedDescription = "Movie description";
        final var expectedIsActive = true;

        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.getIsActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAInValidCommand_whenCallsUpdateCategory_shouldReturnDomainException() {

        final var category = Category.newCategory("Film", null, true);

        final var expectedName = " ";
        final var expectedDescription = "Movie description";
        final var expectedIsActive = true;
        final var expectedExceptionMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedId = category.getId();


        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));


        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedExceptionMessage, notification.firstError().message());

        verify(categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenAInValidCommandWithInactiveCategory_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var category = Category.newCategory("Film", null, true);

        final var expectedName = "Movie";
        final var expectedDescription = "Movie description";
        final var expectedIsActive = false;

        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        assertTrue(category.getIsActive());
        assertNull(category.getDeletedAt());

        final var output = useCase.execute(command).get();

        assertNotNull(output);
        assertNotNull(output.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.getIsActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.nonNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var category = Category.newCategory("Film", null, true);

        final var expectedName = "Movie";
        final var expectedDescription = "Movie description";
        final var expectedIsActive = true;
        final var expectedExceptionMessage = "Random exception";
        final var expectedErrorCount = 1;

        final var expectedId = category.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));

        when(categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedExceptionMessage));

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedExceptionMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.getIsActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && category.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFoundException() {

        final var expectedName = " ";
        final var expectedDescription = "Movie description";
        final var expectedIsActive = true;
        final var expectedExceptionMessage = "Category with ID 123 was not found";
        final var expectedId = "123";

        final var command = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(expectedExceptionMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).findById(CategoryID.from(expectedId));
        verify(categoryGateway, times(0)).update(any());
    }
}
