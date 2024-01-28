package com.postech.catalog.domain.video;

import com.postech.catalog.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {

    private final String value;

    private VideoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static VideoID from(final String value) {
        return new VideoID(value.toLowerCase());
    }

    public static VideoID from(final UUID value) {
        return new VideoID(value.toString());
    }

    public static VideoID unique() {
        return VideoID.from(UUID.randomUUID());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoID that = (VideoID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
