package com.postech.catalog.domain.video;

import com.postech.catalog.domain.validation.Error;
import com.postech.catalog.domain.validation.ValidationHandler;
import com.postech.catalog.domain.validation.Validator;

public class VideoValidator extends Validator {

    private static final int TITLE_MAX_LENGTH = 255;

    private static final int URL_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4_000;

    private final Video video;

    protected VideoValidator(final Video video, final ValidationHandler handler) {
        super(handler);
        this.video = video;
    }

    @Override
    public void validate() {
        checkTitleConstraints();
        checkDescriptionConstraints();
        checkUrlConstraints();
    }

    private void checkTitleConstraints() {
        final var title = this.video.getTitle();
        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
            return;
        }

        if (title.isBlank()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
            return;
        }

        final int length = title.trim().length();
        if (length > TITLE_MAX_LENGTH) {
            this.validationHandler().append(new Error("'title' must be between 1 and 255 characters"));
        }
    }

    private void checkDescriptionConstraints() {
        final var description = this.video.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }

        if (description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }

        final int length = description.trim().length();
        if (length > DESCRIPTION_MAX_LENGTH) {
            this.validationHandler().append(new Error("'description' must be between 1 and 4000 characters"));
        }
    }

    private void checkUrlConstraints() {
        final var url = this.video.getUrl();
        if (url == null) {
            this.validationHandler().append(new Error("'url' should not be null"));
            return;
        }

        if (url.isBlank()) {
            this.validationHandler().append(new Error("'url' should not be empty"));
            return;
        }

        final int length = url.trim().length();
        if (length > URL_MAX_LENGTH) {
            this.validationHandler().append(new Error("'url' must be between 1 and 255 characters"));
        }
    }


}
