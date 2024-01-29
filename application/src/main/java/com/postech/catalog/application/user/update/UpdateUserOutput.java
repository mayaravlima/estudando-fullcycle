package com.postech.catalog.application.user.update;

import com.postech.catalog.domain.user.User;

public record UpdateUserOutput(String id) {

    public static UpdateUserOutput from(final User user) {
        return new UpdateUserOutput(user.getId().getValue());
    }
}
