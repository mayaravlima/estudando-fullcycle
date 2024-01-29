package com.postech.catalog.infrastructure.video.persistence;

import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.utils.CollectionUtils;
import com.postech.catalog.domain.video.Video;
import com.postech.catalog.domain.video.VideoID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "videos")
@Entity(name = "Video")
@Getter
@Setter
public class VideoJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "click_count", nullable = false)
    private Long clickCount;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCategoryJpaEntity> categories;


    public VideoJpaEntity() {
    }

    private VideoJpaEntity(
            final String id,
            final String title,
            final String description,
            final String url,
            final Long clickCount,
            final Instant createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.createdAt = createdAt;
        this.clickCount = clickCount;
        this.categories = new HashSet<>(3);
    }

    public static VideoJpaEntity from(final Video aVideo) {
        final var entity = new VideoJpaEntity(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getUrl(),
                aVideo.getClickCount(),
                aVideo.getCreatedAt()
        );

        aVideo.getCategories()
                .forEach(entity::addCategory);

        return entity;
    }

    public Video toAggregate() {
        return Video.with(
                VideoID.from(getId()),
                getTitle(),
                getDescription(),
                getUrl(),
                getCreatedAt(),
                getClickCount(),
                getCategories().stream()
                        .map(it -> CategoryID.from(it.getId().getCategoryId()))
                        .collect(Collectors.toSet())
        );
    }

    public void addCategory(final CategoryID anId) {
        this.categories.add(VideoCategoryJpaEntity.from(this, anId));
    }

    public Set<CategoryID> getCategoriesID() {
        return CollectionUtils.mapTo(getCategories(), it -> CategoryID.from(it.getId().getCategoryId()));
    }

}
