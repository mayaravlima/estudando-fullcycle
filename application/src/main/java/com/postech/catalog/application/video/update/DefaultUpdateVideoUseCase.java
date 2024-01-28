package com.postech.catalog.application.video.update;

import com.postech.catalog.application.video.create.CreateVideoOutput;
import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.DomainException;
import com.postech.catalog.domain.exceptions.NotFoundException;
import com.postech.catalog.domain.exceptions.NotificationException;
import com.postech.catalog.domain.validation.Error;
import com.postech.catalog.domain.validation.ValidationHandler;
import com.postech.catalog.domain.validation.handler.Notification;
import com.postech.catalog.domain.video.Video;
import com.postech.catalog.domain.video.VideoGateway;
import com.postech.catalog.domain.video.VideoID;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.vavr.API.Try;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {

    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;

    public DefaultUpdateVideoUseCase(
            final VideoGateway videoGateway,
            final CategoryGateway categoryGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateVideoOutput> execute(final UpdateVideoCommand command) {
        final var id = VideoID.from(command.id());
        final var categories = toIdentifier(command.categories(), CategoryID::from);

        final var video = this.videoGateway.findById(id)
                .orElseThrow(notFoundException(id));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));


        video.update(
                command.title(),
                command.description(),
                command.url(),
                categories
        );

        video.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not update Aggregate Video", notification);
        }

        return notification.hasError() ? Either.left(notification) : update(video);

    }

    private Either<Notification, UpdateVideoOutput> update(final Video video) {
        return Try(() -> this.videoGateway.update(video))
                .toEither()
                .bimap(Notification::create, UpdateVideoOutput::from);
    }

    private Supplier<DomainException> notFoundException(final VideoID id) {
        return () -> NotFoundException.with(Video.class, id);
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
