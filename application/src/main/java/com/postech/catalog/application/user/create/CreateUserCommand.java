package com.postech.catalog.application.user.create;

import java.util.Set;

public record CreateUserCommand(
        String name,
        String email,
        Set<String> favorites
) {

    public static CreateUserCommand with(
            final String name,
            final String email,
            final Set<String> favorites
    ) {
        return new CreateUserCommand(
                name,
                email,
                favorites
        );
    }
}
