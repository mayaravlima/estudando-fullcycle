package com.postech.catalog.domain.user;

import com.postech.catalog.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class UserID extends Identifier {

    private final String value;

    private UserID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static UserID unique() {
        return UserID.from(UUID.randomUUID());
    }

    public static UserID from(final String value) {
        return new UserID(value);
    }

    public static UserID from(final UUID value) {
        return new UserID(value.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserID that = (UserID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

}
