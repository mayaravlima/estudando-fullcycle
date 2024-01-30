package com.postech.catalog.domain.video;

import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    @Test
    public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        final var expectedTitle = "New Movie";
        final var expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                """;
        final var expectedURL = "https://www.youtube.com/watch?v=1";
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedClickCount = 1L;

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedURL,
                expectedClickCount,
                expectedCategories
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedCategories, actualVideo.getCategories());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() throws InterruptedException {
        final var expectedTitle = "New Movie";
        final var expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                """;
        final var expectedURL = "https://www.youtube.com/watch?v=1";
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedClickCount = 1L;


        final var video = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedURL,
                expectedClickCount,
                expectedCategories
        );

        Thread.sleep(10);

        final var actualVideo = Video.with(video).update(
                expectedTitle,
                expectedDescription,
                expectedURL,
                expectedClickCount,
                expectedCategories
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedCategories, actualVideo.getCategories());


        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

}
