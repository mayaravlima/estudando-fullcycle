package com.postech.catalog.application.category.update;

import com.postech.catalog.application.UseCase;
import com.postech.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
