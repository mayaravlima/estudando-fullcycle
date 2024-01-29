package com.postech.catalog.application.user.update;

import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.exceptions.DomainException;
import com.postech.catalog.domain.exceptions.NotFoundException;
import com.postech.catalog.domain.exceptions.NotificationException;
import com.postech.catalog.domain.user.User;
import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.user.UserID;
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

public class DefaultUpdateUserUseCase extends UpdateUserUseCase {

    private final VideoGateway videoGateway;
    private final UserGateway userGateway;

    public DefaultUpdateUserUseCase(
            final UserGateway userGateway,
            final VideoGateway videoGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Either<Notification, UpdateUserOutput> execute(final UpdateUserCommand command) {
        final var id = UserID.from(command.id());
        final var videos = toIdentifier(command.favorites(), VideoID::from);

        final var user = this.userGateway.findById(id)
                .orElseThrow(notFoundException(id));

        final var notification = Notification.create();
        notification.append(validateVideos(videos));


        user.update(
                command.name(),
                command.email(),
                videos
        );

        user.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not update Aggregate Video", notification);
        }

        return notification.hasError() ? Either.left(notification) : update(user);

    }

    private Either<Notification, UpdateUserOutput> update(final User user) {
        return Try(() -> this.userGateway.update(user))
                .toEither()
                .bimap(Notification::create, UpdateUserOutput::from);
    }

    private Supplier<DomainException> notFoundException(final UserID id) {
        return () -> NotFoundException.with(Video.class, id);
    }

    private <T extends Identifier> ValidationHandler validateVideos(final Set<VideoID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = videoGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some videos could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
