package com.postech.catalog.infrastructure.user.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.postech.catalog.domain.video.VideoPreview;

import java.util.List;

public record UserRecommendationResponse(
        @JsonProperty("category") String category,
        @JsonProperty("videos") List<VideoPreview> videos
) {
}
