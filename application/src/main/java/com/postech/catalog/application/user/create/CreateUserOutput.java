package com.postech.catalog.application.user.create;

import com.postech.catalog.domain.user.User;

public record CreateUserOutput(String id) {

    public static CreateUserOutput from(final User user) {
        return new CreateUserOutput(user.getId().getValue());
    }
}