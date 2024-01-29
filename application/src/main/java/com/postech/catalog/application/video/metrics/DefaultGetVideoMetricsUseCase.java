package com.postech.catalog.application.video.metrics;

import com.postech.catalog.domain.video.VideoGateway;

import java.util.Objects;

public class DefaultGetVideoMetricsUseCase extends GetVideoMetricsUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoMetricsUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoMetricsOutput execute() {
        return VideoMetricsOutput.from(this.videoGateway.getMetrics());
    }
}
