package com.postech.catalog.application.video.retrieve.list;

import com.postech.catalog.application.UseCase;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase
        extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
