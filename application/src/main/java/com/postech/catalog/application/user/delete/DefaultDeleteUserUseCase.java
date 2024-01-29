package com.postech.catalog.application.user.delete;

import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.user.UserID;

import java.util.Objects;

public class DefaultDeleteUserUseCase extends DeleteUserUseCase {

    private final UserGateway userGateway;

    public DefaultDeleteUserUseCase(
            final UserGateway userGateway
    ) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public void execute(final String id) {
        final var userId = UserID.from(id);
        this.userGateway.deleteById(userId);
    }
}
