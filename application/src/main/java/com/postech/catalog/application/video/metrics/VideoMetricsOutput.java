package com.postech.catalog.application.video.metrics;

import com.postech.catalog.domain.video.VideoMetrics;

public record VideoMetricsOutput(
        long total,
        long favorites,
        double average
) {

    public static VideoMetricsOutput from(final VideoMetrics videoMetrics) {
        return new VideoMetricsOutput(
                videoMetrics.getTotal(),
                videoMetrics.getFavorites(),
                videoMetrics.getAverage()
        );
    }
}
