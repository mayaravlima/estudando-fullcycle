package com.postech.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record VideoListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("url") String url,

        @JsonProperty("click_count") Long clickCount,
        @JsonProperty("created_at") Instant createdAt

) {
}
