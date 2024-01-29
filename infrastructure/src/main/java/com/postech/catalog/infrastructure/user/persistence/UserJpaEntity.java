package com.postech.catalog.infrastructure.user.persistence;

import com.postech.catalog.domain.user.User;
import com.postech.catalog.domain.user.UserID;
import com.postech.catalog.domain.video.VideoID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "users")
@Entity(name = "User")
@Getter
@Setter
public class UserJpaEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserVideoJpaEntity> videos;


    public UserJpaEntity() {
    }

    public UserJpaEntity(
            String id,
            String name,
            String email,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.videos = new HashSet<>(3);
    }

    public static UserJpaEntity from(User user) {
        final var entity = new UserJpaEntity(
                user.getId().getValue(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
        user.getFavorites().forEach(entity::addFavorite);

        return entity;
    }

    public void addFavorite(final VideoID anId) {
        this.videos.add(UserVideoJpaEntity.from(this, anId));
    }

    public User toAggregate() {
        return User.with(
                UserID.from(getId()),
                getName(),
                getEmail(),
                getCreatedAt(),
                getVideos().stream()
                        .map(it -> VideoID.from(it.getId().getVideoId()))
                        .collect(Collectors.toSet())
        );
    }

}
