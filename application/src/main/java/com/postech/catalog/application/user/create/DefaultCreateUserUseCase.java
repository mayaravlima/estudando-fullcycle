package com.postech.catalog.application.user.create;

import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.exceptions.NotificationException;
import com.postech.catalog.domain.user.User;
import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.validation.Error;
import com.postech.catalog.domain.validation.ValidationHandler;
import com.postech.catalog.domain.validation.handler.Notification;
import com.postech.catalog.domain.video.VideoGateway;
import com.postech.catalog.domain.video.VideoID;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateUserUseCase extends CreateUserUseCase {
    private final UserGateway userGateway;
    private final VideoGateway videoGateway;

    public DefaultCreateUserUseCase(
            final UserGateway userGateway,
            final VideoGateway videoGateway
    ) {
        this.userGateway = Objects.requireNonNull(userGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Either<Notification, CreateUserOutput> execute(final CreateUserCommand command) {
        final var videos = Objects.nonNull(command.favorites())
               ? toIdentifier(command.favorites(), VideoID::from)
                : null;

        final var notification = Notification.create();
        notification.append(validateVideos(videos));

        final var user = User.newUser(
                command.name(),
                command.email(),
                videos
        );

        user.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not create video", notification);
        }

        return notification.hasError() ? Left(notification) : create(user);
    }

    private Either<Notification, CreateUserOutput> create(final User user) {
        return Try(() -> this.userGateway.create(user))
                .toEither()
                .bimap(Notification::create, CreateUserOutput::from);
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
