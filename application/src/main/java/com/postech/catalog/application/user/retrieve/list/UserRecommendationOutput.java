package com.postech.catalog.application.user.retrieve.list;

import com.postech.catalog.domain.video.VideoPreview;

import java.util.List;

public record UserRecommendationOutput(
        List<VideoPreview> videos
) {

    public static UserRecommendationOutput from(final List<VideoPreview> videos) {
        return new UserRecommendationOutput(videos);
    }
}
