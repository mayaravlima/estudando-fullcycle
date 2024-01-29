package com.postech.catalog.domain.user;

import java.time.Instant;

public record UserPreview (
        String id,
        String name,
        String email,
        Instant createdAt
) {
}
