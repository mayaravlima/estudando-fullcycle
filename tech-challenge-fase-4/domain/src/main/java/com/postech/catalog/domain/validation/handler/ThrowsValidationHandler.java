package com.postech.catalog.domain.validation.handler;

import com.postech.catalog.domain.exceptions.DomainException;
import com.postech.catalog.domain.validation.Error;
import com.postech.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public ValidationHandler validate(Validation validation) {
        try {
            validation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new Error(ex.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return null;
    }

    @Override
    public boolean hasError() {
        return ValidationHandler.super.hasError();
    }
}
