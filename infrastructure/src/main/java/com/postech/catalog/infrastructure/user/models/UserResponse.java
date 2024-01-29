package com.postech.catalog.infrastructure.user.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public record UserResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("favorites") Set<String> favorites
) {
}
