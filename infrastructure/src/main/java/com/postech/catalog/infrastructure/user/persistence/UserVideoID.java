package com.postech.catalog.infrastructure.user.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserVideoID implements Serializable {
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "video_id", nullable = false)
    private String videoId;

    public UserVideoID() {
    }

    private UserVideoID(final String userId, final String videoId) {
        this.userId = userId;
        this.videoId = videoId;
    }

    public static UserVideoID from(final String userId, final String videoId) {
        return new UserVideoID(userId, videoId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVideoID that = (UserVideoID) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getVideoId(), that.getVideoId());
    }

    public String getUserId() {
        return userId;
    }

    public UserVideoID setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserVideoID setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public String getVideoId() {
        return videoId;
    }


}
