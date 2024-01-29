package com.postech.catalog.application.user.update;

import com.postech.catalog.application.UseCase;
import com.postech.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateUserUseCase
        extends UseCase<UpdateUserCommand, Either<Notification, UpdateUserOutput>> {
}
