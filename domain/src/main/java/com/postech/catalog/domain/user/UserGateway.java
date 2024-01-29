package com.postech.catalog.domain.user;

import com.postech.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface UserGateway {

        User create(User aUser);

        void deleteById(UserID anId);

        User update(User aUser);

        Optional<User> findById(UserID anId);

        Pagination<UserPreview> findAll(UserSearchQuery aQuery);
}
