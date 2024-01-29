package com.postech.catalog.application.user.retrieve.get;

import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.user.User;
import com.postech.catalog.domain.utils.CollectionUtils;

import java.time.Instant;
import java.util.Set;

public record UserOutput(
        String id,
        String name,
        String email,
        Instant createdAt,
        Set<String> favorites
) {

    public static UserOutput from(final User user) {
        return new UserOutput(
                user.getId().getValue(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                CollectionUtils.mapTo(user.getFavorites(), Identifier::getValue)
        );
    }
}
