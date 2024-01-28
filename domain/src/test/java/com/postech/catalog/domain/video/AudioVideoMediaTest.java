package com.postech.catalog.domain.video;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AudioVideoMediaTest {
    @Test
    public void givenValidParams_whenCallsNewAudioVideo_ShouldReturnInstance() {
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedRawLocation = "/images/ac";
        final var expectedEncodedLocation = "/images/ac-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var actualVideo =
                AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);

        assertNotNull(actualVideo);
        assertEquals(expectedChecksum, actualVideo.checksum());
        assertEquals(expectedName, actualVideo.name());
        assertEquals(expectedRawLocation, actualVideo.rawLocation());
        assertEquals(expectedEncodedLocation, actualVideo.encodedLocation());
        assertEquals(expectedStatus, actualVideo.status());
    }

    @Test
    public void givenTwoVideosWithSameChecksumAndLocation_whenCallsEquals_ShouldReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedRawLocation = "/images/ac";

        final var img1 =
                AudioVideoMedia.with(expectedChecksum, "Random", expectedRawLocation, "", MediaStatus.PENDING);

        final var img2 =
                AudioVideoMedia.with(expectedChecksum, "Simple", expectedRawLocation, "", MediaStatus.PENDING);

        assertEquals(img1, img2);
        assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_ShouldReturnError() {

        assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with("abc", null, "/videos", "/videos", MediaStatus.PENDING)
        );

        assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with("abc", "Random", null, "/videos", MediaStatus.PENDING)
        );

        assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with("abc", "Random", "/videos", null, MediaStatus.PENDING)
        );

        assertThrows(
                NullPointerException.class,
                () -> AudioVideoMedia.with("abc", "Random", "/videos", "/videos", null)
        );
    }
}
