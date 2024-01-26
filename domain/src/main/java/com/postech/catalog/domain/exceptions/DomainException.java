package com.postech.catalog.domain.exceptions;

import com.postech.catalog.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceException {

    protected final List<Error> errors;

    protected DomainException(final String message, final List<Error> error) {
        super(message);
        this.errors = error;
    }

    public static DomainException with(final Error error) {
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<Error> error) {
        return new DomainException("", error);
    }

    public List<Error> getErrors() {
        return errors;
    }

}
