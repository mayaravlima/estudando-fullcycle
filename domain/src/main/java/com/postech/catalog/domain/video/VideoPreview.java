package com.postech.catalog.domain.video;

import java.time.Instant;

public record VideoPreview(
        String id,
        String title,
        String url,
        String description,
        Long clickCount,
        Instant createdAt
) {

    public VideoPreview(final Video video) {
        this(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getUrl(),
                video.getClickCount(),
                video.getCreatedAt()
        );
    }
}
