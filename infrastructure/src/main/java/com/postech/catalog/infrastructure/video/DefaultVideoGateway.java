package com.postech.catalog.infrastructure.video;

import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.video.*;
import com.postech.catalog.infrastructure.user.persistence.UserRepository;
import com.postech.catalog.infrastructure.utils.SqlUtils;
import com.postech.catalog.infrastructure.video.persistence.VideoJpaEntity;
import com.postech.catalog.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.postech.catalog.domain.utils.CollectionUtils.mapTo;
import static com.postech.catalog.domain.utils.CollectionUtils.nullIfEmpty;

@Component
public class DefaultVideoGateway implements VideoGateway {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public DefaultVideoGateway(
            final VideoRepository videoRepository,
            final UserRepository userRepository
    ) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    @Override
    @Transactional
    public Video create(final Video video) {
        return save(video);
    }

    @Override
    public void deleteById(final VideoID id) {
        final var videoId = id.getValue();
        if (this.videoRepository.existsById(videoId)) {
            this.videoRepository.deleteById(videoId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID id) {
        return this.videoRepository.findById(id.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    @Transactional
    public Video update(final Video video) {
        return save(video);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                SqlUtils.like(SqlUtils.upper(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    @Override
    public List<VideoID> existsByIds(Iterable<VideoID> videoIDs) {
        final var ids = StreamSupport.stream(videoIDs.spliterator(), false)
                .map(VideoID::getValue)
                .toList();

        return this.videoRepository.existsByIds(ids).stream()
                .map(VideoID::from)
                .toList();
    }

    @Override
    public VideoMetrics getMetrics() {
        final var totalOfVideos = this.videoRepository.totalOfVideos();
        final var totalFavorites = this.userRepository.totalFavorites();
        final var averageViews = this.videoRepository.averageClickCount();
        return VideoMetrics.from(totalOfVideos, totalFavorites, averageViews);
    }

    @Override
    @Transactional
    public void deleteVideoFromUserVideo(VideoID videoId) {
        this.videoRepository.deleteVideoFromUserVideo(videoId.getValue());
    }

    private Video save(final Video video) {

        return this.videoRepository.save(VideoJpaEntity.from(video))
                .toAggregate();
    }
}
