package com.postech.catalog.application.video.delete;

import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.video.VideoGateway;
import com.postech.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultDeleteVideoUseCase(
            final VideoGateway videoGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final String id) {
        final var videoId = VideoID.from(id);
        this.videoGateway.deleteVideoFromUserVideo(videoId);
        this.videoGateway.deleteById(videoId);
    }
}
