package com.postech.catalog.application.video.create;

import com.postech.catalog.application.UseCase;
import com.postech.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateVideoUseCase
        extends UseCase<CreateVideoCommand, Either<Notification, CreateVideoOutput>> {
}
