package com.postech.catalog.application.user.retrieve.get;

import com.postech.catalog.domain.exceptions.NotFoundException;
import com.postech.catalog.domain.user.User;
import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.user.UserID;

import java.util.Objects;

public class DefaultGetUserByIdUseCase extends GetUserByIdUseCase {

    private final UserGateway userGateway;

    public DefaultGetUserByIdUseCase(final UserGateway userGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public UserOutput execute(final String id) {
        final var userId = UserID.from(id);
        return this.userGateway.findById(userId)
                .map(UserOutput::from)
                .orElseThrow(() -> NotFoundException.with(User.class, userId));
    }
}
