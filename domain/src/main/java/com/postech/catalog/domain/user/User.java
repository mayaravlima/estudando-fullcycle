package com.postech.catalog.domain.user;

import com.postech.catalog.domain.AggregateRoot;
import com.postech.catalog.domain.validation.ValidationHandler;
import com.postech.catalog.domain.video.VideoID;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

public class User extends AggregateRoot<UserID> {

    private String name;

    private String email;

    private Instant createdAt;

    private Set<VideoID> favorites;

    protected User(
            final UserID userID,
            final String name,
            final String email,
            final Instant createdAt,
            final Set<VideoID> favorites
    ) {
        super(userID);
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.favorites = favorites;
    }

    public static User newUser(
            final String name,
            final String email,
            final Set<VideoID> favorites
    ) {
        final var id = UserID.unique();
        final var now = Instant.now();
        return new User(
                id,
                name,
                email,
                now,
                favorites
        );
    }

    public static User with(
            final UserID userID,
            final String name,
            final String email,
            final Instant createdAt,
            final Set<VideoID> favorites
    ) {
        return new User(
                userID,
                name,
                email,
                createdAt,
                favorites
        );
    }

    public User update(
            final String name,
            final String email,
            final Set<VideoID> favorites
    ) {
        this.name = name;
        this.email = email;
        this.setFavorites(favorites);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Set<VideoID> getFavorites() {
        return favorites != null ? Collections.unmodifiableSet(favorites) : Collections.emptySet();
    }

    public void setFavorites(Set<VideoID> favorites) {
        this.favorites = favorites;
    }

    @Override
    public void validate(ValidationHandler handler) {

    }
}
