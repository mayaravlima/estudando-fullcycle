package com.postech.catalog.infrastructure.user.persistence;

import com.postech.catalog.domain.video.VideoID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity(name = "UserVideo")
@Table(name = "users_videos")
public class UserVideoJpaEntity {

    @EmbeddedId
    private UserVideoID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private UserJpaEntity user;

    public UserVideoJpaEntity() {
    }

    private UserVideoJpaEntity(final UserVideoID id, final UserJpaEntity user) {
        this.id = id;
        this.user = user;
    }

    public static UserVideoJpaEntity from(final UserJpaEntity user, final VideoID video) {
        return new UserVideoJpaEntity(
                UserVideoID.from(user.getId(), video.getValue()),
                user
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVideoJpaEntity that = (UserVideoJpaEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser());
    }
}
