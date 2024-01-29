package com.postech.catalog.application.user.retrieve.list;

import com.postech.catalog.application.UseCase;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.user.UserSearchQuery;

public abstract class ListUsersUseCase
        extends UseCase<UserSearchQuery, Pagination<UserListOutput>> {
}
