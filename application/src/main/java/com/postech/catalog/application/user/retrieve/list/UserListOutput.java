package com.postech.catalog.application.user.retrieve.list;


import com.postech.catalog.domain.user.User;
import com.postech.catalog.domain.user.UserPreview;

import java.time.Instant;

public record UserListOutput(
        String id,
        String name,
        String email,
        Instant createdAt
) {

    public static UserListOutput from(final User user) {
        return new UserListOutput(
                user.getId().getValue(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()

        );
    }

    public static UserListOutput from(final UserPreview user) {
        return new UserListOutput(
                user.id(),
                user.name(),
                user.email(),
                user.createdAt()
        );
    }
}
