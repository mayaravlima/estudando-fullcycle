package com.postech.catalog.application.video.retrieve.get;

import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.utils.CollectionUtils;
import com.postech.catalog.domain.video.Video;

import java.time.Instant;
import java.util.Set;

public record VideoOutput(
        String id,
        Instant createdAt,
        String title,
        String description,
        String url,
        Set<String> categories
) {

    public static VideoOutput from(final Video video) {
        return new VideoOutput(
                video.getId().getValue(),
                video.getCreatedAt(),
                video.getTitle(),
                video.getDescription(),
                video.getUrl(),
                CollectionUtils.mapTo(video.getCategories(), Identifier::getValue)
        );
    }
}
