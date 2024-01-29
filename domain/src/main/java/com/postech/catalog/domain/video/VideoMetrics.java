package com.postech.catalog.domain.video;

public class VideoMetrics {
    long total;
    long favorites;

    double average;

    public VideoMetrics(
            final long total,
            final long favorites,
            final double average
    ) {
        this.total = total;
        this.favorites = favorites;
        this.average = average;
    }

    public long getTotal() {
        return this.total;
    }

    public long getFavorites() {
        return this.favorites;
    }

    public double getAverage() {
        return average;
    }

    public void setTotal(final int total) {
        this.total = total;
    }

    public void setFavorites(final int favorites) {
        this.favorites = favorites;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public static VideoMetrics from(
            final long total,
            final long favorites,
            final double average
    ) {
        return new VideoMetrics(total, favorites, average);
    }
}
