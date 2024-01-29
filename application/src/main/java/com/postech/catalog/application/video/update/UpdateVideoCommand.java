package com.postech.catalog.application.video.update;

import java.util.Set;

public record UpdateVideoCommand(
        String id,
        String title,
        String description,
        String url,
        Set<String> categories

) {

    public static UpdateVideoCommand with(
            final String id,
            final String title,
            final String description,
            final String url,
            final Set<String> categories
    ) {
        return new UpdateVideoCommand(
                id,
                title,
                description,
                url,
                categories
        );
    }

}
