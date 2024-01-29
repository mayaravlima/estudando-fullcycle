package com.postech.catalog.application.user.create;

import com.postech.catalog.application.UseCase;
import com.postech.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateUserUseCase
        extends UseCase<CreateUserCommand, Either<Notification, CreateUserOutput>> {
}
