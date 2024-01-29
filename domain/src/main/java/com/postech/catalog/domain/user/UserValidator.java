package com.postech.catalog.domain.user;

import com.postech.catalog.domain.validation.Error;
import com.postech.catalog.domain.validation.ValidationHandler;
import com.postech.catalog.domain.validation.Validator;

public class UserValidator extends Validator {
    private static final int NAME_MAX_LENGTH = 255;

    private static final int EMAIL_MAX_LENGTH = 255;

    private final User user;

    protected UserValidator(final User user, ValidationHandler handler) {
        super(handler);
        this.user = user;
    }


    @Override
    public void validate() {
        checkEmailConstraints();
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.user.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 1 and 255 characters"));
        }
    }

    private void checkEmailConstraints() {
        final var email = this.user.getEmail();
        if (email == null) {
            this.validationHandler().append(new Error("'email' should not be null"));
            return;
        }

        if (email.isBlank()) {
            this.validationHandler().append(new Error("'email' should not be empty"));
            return;
        }

        final int length = email.trim().length();
        if (length > EMAIL_MAX_LENGTH) {
            this.validationHandler().append(new Error("'email' must be between 1 and 255 characters"));
        }
    }
}
