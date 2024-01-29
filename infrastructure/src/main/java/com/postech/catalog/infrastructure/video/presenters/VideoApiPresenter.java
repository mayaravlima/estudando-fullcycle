package com.postech.catalog.infrastructure.video.presenters;

import com.postech.catalog.application.video.retrieve.get.VideoOutput;
import com.postech.catalog.application.video.retrieve.list.VideoListOutput;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.infrastructure.video.models.VideoListResponse;
import com.postech.catalog.infrastructure.video.models.VideoResponse;

import java.util.List;
import java.util.stream.Collectors;

public interface VideoApiPresenter {
    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.url(),
                output.createdAt(),
                output.categories()
        );
    }

    static VideoListResponse present(final VideoListOutput output) {
        return new VideoListResponse(
                output.id(),
                output.title(),
                output.description(),
                output.url(),
                output.createdAt()
        );
    }

   static Pagination<VideoListResponse> present(Pagination<VideoListOutput> videoListOutput) {
        List<VideoListOutput> content = videoListOutput.items();
        List<VideoListResponse> categoryListResponses = content.stream()
                .map(VideoApiPresenter::convertToResponse)
                .collect(Collectors.toList());

        return new Pagination<>(
                videoListOutput.currentPage(),
                videoListOutput.perPage(),
                videoListOutput.total(),
                categoryListResponses
        );
    }

    private static VideoListResponse convertToResponse(VideoListOutput categoryListOutput) {
        return new VideoListResponse(
                categoryListOutput.id(),
                categoryListOutput.title(),
                categoryListOutput.description(),
                categoryListOutput.url(),
                categoryListOutput.createdAt()
        );
    }
}
