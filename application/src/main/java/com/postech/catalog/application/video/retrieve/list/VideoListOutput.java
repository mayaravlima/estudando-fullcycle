package com.postech.catalog.application.video.retrieve.list;

import com.postech.catalog.domain.video.Video;
import com.postech.catalog.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(
        String id,
        String title,
        String description,
        String url,
        Long clickCount,
        Instant createdAt
) {

    public static VideoListOutput from(final Video video) {
        return new VideoListOutput(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getUrl(),
                video.getClickCount(),
                video.getCreatedAt()

        );
    }

    public static VideoListOutput from(final VideoPreview video) {
        return new VideoListOutput(
                video.id(),
                video.title(),
                video.description(),
                video.url(),
                video.clickCount(),
                video.createdAt()
        );
    }
}
