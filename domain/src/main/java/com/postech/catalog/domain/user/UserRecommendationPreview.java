package com.postech.catalog.domain.user;

import com.postech.catalog.domain.video.VideoPreview;

import java.util.Set;

public record UserRecommendationPreview(
        String id,
        Set<VideoPreview> videos
) {
}
