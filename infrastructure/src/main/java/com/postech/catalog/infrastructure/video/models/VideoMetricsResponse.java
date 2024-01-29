package com.postech.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMetricsResponse (
        @JsonProperty("total_videos") long total,
        @JsonProperty("total_favorites") long favorites,
        @JsonProperty("average_visualizations") double average
) {
}
