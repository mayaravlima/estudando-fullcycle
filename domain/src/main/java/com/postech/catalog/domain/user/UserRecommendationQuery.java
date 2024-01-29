package com.postech.catalog.domain.user;

public record UserRecommendationQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
