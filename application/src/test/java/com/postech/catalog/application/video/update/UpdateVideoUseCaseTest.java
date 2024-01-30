package com.postech.catalog.application.video.update;

import com.postech.catalog.application.UseCaseTest;

import com.postech.catalog.application.video.create.DefaultCreateVideoUseCase;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.NotificationException;
import com.postech.catalog.domain.video.Video;
import com.postech.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UpdateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() {
        final var video = Video.newVideo(
                "Movie",
                "Description",
                "https://www.youtube.com/watch?v=1",
                0L,
                Set.of(CategoryID.from("1"))
        );
        final var expectedTitle = "Movie";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=1";
        final var expectedClickCount = 0L;
        final var expectedCategories = Set.of(CategoryID.from("1"));
        final var expectedId = video.getId();

        final var aCommand = UpdateVideoCommand.with(
                expectedId.getValue(),
                expectedTitle,
                expectedDescription,
                expectedUrl,
                expectedClickCount,
                Set.of(CategoryID.from("1").getValue())
        );

        when(videoGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(video.clone()));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualResult = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).update(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedUrl, actualVideo.getUrl())
                        && Objects.equals(expectedClickCount, actualVideo.getClickCount())
        ));
    }
}
