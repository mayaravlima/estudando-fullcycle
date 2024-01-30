package com.postech.catalog.application.video.create;

import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.NotificationException;
import com.postech.catalog.domain.validation.Error;
import com.postech.catalog.domain.validation.ValidationHandler;
import com.postech.catalog.domain.validation.handler.Notification;
import com.postech.catalog.domain.video.Video;
import com.postech.catalog.domain.video.VideoGateway;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final VideoGateway videoGateway;

    public DefaultCreateVideoUseCase(
            final CategoryGateway categoryGateway,
            final VideoGateway videoGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Either<Notification, CreateVideoOutput> execute(final CreateVideoCommand command) {
        final var clickCount = Objects.nonNull(command.clickCount())
                ? command.clickCount()
                : 0L;
        final var categories = toIdentifier(command.categories(), CategoryID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));

        final var video = Video.newVideo(
                command.title(),
                command.description(),
                command.url(),
                clickCount,
                categories
        );

        video.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not create video", notification);
        }

        return notification.hasError() ? Left(notification) : create(video);
    }

    private Either<Notification, CreateVideoOutput> create(final Video video) {
        return Try(() -> this.videoGateway.create(video))
                .toEither()
                .bimap(Notification::create, CreateVideoOutput::from);
    }


    private <T extends Identifier> ValidationHandler validateCategories(final Set<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }
        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);
            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));
            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }
        return notification;
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
