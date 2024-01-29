package com.postech.catalog.domain.user;

import com.postech.catalog.domain.video.VideoID;

import java.util.Set;

public record UserSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<VideoID> favorites
) {
}
