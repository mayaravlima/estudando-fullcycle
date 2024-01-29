package com.postech.catalog.application.user.update;

import java.util.Set;

public record UpdateUserCommand(
        String id,
        String name,
        String email,
        Set<String> favorites

) {

    public static UpdateUserCommand with(
            final String id,
            final String name,
            final String email,
            final Set<String> favorites
    ) {
        return new UpdateUserCommand(
                id,
                name,
                email,
                favorites
        );
    }

}
