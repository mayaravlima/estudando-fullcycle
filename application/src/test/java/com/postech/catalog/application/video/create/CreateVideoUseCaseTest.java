package com.postech.catalog.application.video.create;

import com.postech.catalog.application.UseCaseTest;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.NotificationException;
import com.postech.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
        final var expectedTitle = "Movie";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=1";
        final var expectedClickCount = 0L;
        final var expectedCategories = Set.of(CategoryID.from("1"));

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedUrl,
                expectedClickCount,
                Set.of("1")
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.get().id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedUrl, actualVideo.getUrl())
                        && Objects.equals(expectedClickCount, actualVideo.getClickCount())
        ));
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoWithoutClickCount_shouldReturnVideoId() {
        final var expectedTitle = "Movie";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=1";
        final var expectedCategories = Set.of(CategoryID.from("1"));

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedUrl,
                null,
                Set.of("1")
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.get().id());

        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedUrl, actualVideo.getUrl())
                        && Objects.equals(0L, actualVideo.getClickCount())
        ));
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoWithCategoriesNotFound_shouldNotReturnVideoId() {
        final var expectedTitle = "Movie";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=1";
        final var expectedClickCount = 0L;
        final var expectedCategories = Set.of("1");

        final var expectedErrorMessage = "Some categories could not be found: 1";
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedUrl,
                expectedClickCount,
                expectedCategories
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());


        final var actualException = assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoWithNullName_shouldNotReturnVideoId() {
        final String expectedTitle = null;
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=1";
        final var expectedClickCount = 0L;
        final var expectedCategories = Set.of(CategoryID.from("1"));

        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedUrl,
                expectedClickCount,
                Set.of("1")
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));


        final var actualException = assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(videoGateway, times(0)).create(any());
    }


}
