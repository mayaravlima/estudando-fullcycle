package com.postech.catalog.domain.video;

import com.postech.catalog.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface VideoGateway {

    Video create(Video aVideo);

    void deleteById(VideoID anId);

    Optional<Video> findById(VideoID anId);

    Video update(Video aVideo);

    Pagination<VideoPreview> findAll(VideoSearchQuery aQuery);

    List<VideoID> existsByIds(Iterable<VideoID> ids);

}
