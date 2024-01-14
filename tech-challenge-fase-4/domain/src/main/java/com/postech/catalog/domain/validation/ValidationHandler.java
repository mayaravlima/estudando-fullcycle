package com.postech.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error error);

    ValidationHandler append(ValidationHandler handler);

    ValidationHandler validate(Validation handler);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    };

    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        }
        return null;
    };

    interface Validation {
        void validate();
    }
}
