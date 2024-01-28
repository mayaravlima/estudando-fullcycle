package com.postech.catalog.application.video.retrieve.get;

import com.postech.catalog.domain.exceptions.NotFoundException;
import com.postech.catalog.domain.video.Video;
import com.postech.catalog.domain.video.VideoGateway;
import com.postech.catalog.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String id) {
        final var videoId = VideoID.from(id);
        return this.videoGateway.findById(videoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, videoId));
    }
}
