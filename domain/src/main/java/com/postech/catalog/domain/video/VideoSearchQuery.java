package com.postech.catalog.domain.video;

import com.postech.catalog.domain.catagory.CategoryID;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<CategoryID> categories
) {
}
