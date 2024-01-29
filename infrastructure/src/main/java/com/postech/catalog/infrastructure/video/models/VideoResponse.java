package com.postech.catalog.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public record VideoResponse(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("url") String url,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("categories_id") Set<String> categoriesId

) {
}
