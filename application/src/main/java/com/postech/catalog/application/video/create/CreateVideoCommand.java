package com.postech.catalog.application.video.create;

import java.util.Optional;
import java.util.Set;

public record CreateVideoCommand(
        String title,
        String description,
        String url,
        Set<String> categories
) {

    public static CreateVideoCommand with(
            final String title,
            final String description,
            final String url,
            final Set<String> categories
    ) {
        return new CreateVideoCommand(
                title,
                description,
                url,
                categories
        );
    }
}
