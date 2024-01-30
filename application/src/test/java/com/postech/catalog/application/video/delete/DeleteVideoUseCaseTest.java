package com.postech.catalog.application.video.delete;

import com.postech.catalog.application.UseCaseTest;
import com.postech.catalog.domain.exceptions.InternalErrorException;
import com.postech.catalog.domain.video.VideoGateway;
import com.postech.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteVideoUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        final var expectedId = VideoID.unique();

        doNothing()
                .when(videoGateway).deleteById(any());
        doNothing()
                .when(videoGateway).deleteVideoFromUserVideo(any());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        verify(videoGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        final var expectedId = VideoID.from("1231");

        doNothing()
                .when(videoGateway).deleteById(any());
        doNothing()
                .when(videoGateway).deleteVideoFromUserVideo(any());

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        verify(videoGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {

        final var expectedId = VideoID.from("1231");

        doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
                .when(videoGateway).deleteById(any());

        Assertions.assertThrows(
                InternalErrorException.class,
                () -> this.useCase.execute(expectedId.getValue())
        );

        verify(videoGateway).deleteById(eq(expectedId));
    }
}
