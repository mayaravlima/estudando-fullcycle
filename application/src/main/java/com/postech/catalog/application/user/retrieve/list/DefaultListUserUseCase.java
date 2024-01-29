package com.postech.catalog.application.user.retrieve.list;

import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.user.UserSearchQuery;

import java.util.Objects;

public class DefaultListUserUseCase extends ListUsersUseCase {

    private final UserGateway userGateway;

    public DefaultListUserUseCase(final UserGateway userGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Pagination<UserListOutput> execute(final UserSearchQuery aQuery) {
        return this.userGateway.findAll(aQuery)
                .map(UserListOutput::from);
    }

}

