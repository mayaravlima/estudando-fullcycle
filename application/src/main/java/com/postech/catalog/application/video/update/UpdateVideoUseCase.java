package com.postech.catalog.application.video.update;

import com.postech.catalog.application.UseCase;
import com.postech.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateVideoUseCase
        extends UseCase<UpdateVideoCommand, Either<Notification, UpdateVideoOutput>> {
}
