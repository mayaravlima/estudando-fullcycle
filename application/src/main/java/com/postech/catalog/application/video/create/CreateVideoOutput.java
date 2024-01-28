package com.postech.catalog.application.video.create;

import com.postech.catalog.domain.video.Video;

public record CreateVideoOutput(String id) {

    public static CreateVideoOutput from(final Video video) {
        return new CreateVideoOutput(video.getId().getValue());
    }
}