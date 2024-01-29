package com.postech.catalog.infrastructure.user.presenters;

import com.postech.catalog.application.user.retrieve.get.UserOutput;
import com.postech.catalog.application.user.retrieve.list.UserListOutput;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.infrastructure.user.models.UserListResponse;
import com.postech.catalog.infrastructure.user.models.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

public interface UserApiPresenter {
    static UserResponse present(final UserOutput output) {
        return new UserResponse(
                output.id(),
                output.name(),
                output.email(),
                output.createdAt(),
                output.favorites()
        );
    }

    static UserListResponse present(final UserListOutput output) {
        return new UserListResponse(
                output.id(),
                output.name(),
                output.email(),
                output.createdAt()
        );
    }

    public static Pagination<UserListResponse> present(Pagination<UserListOutput> userListOutput) {
        List<UserListOutput> content = userListOutput.items();
        List<UserListResponse> favoriteListResponse = content.stream()
                .map(UserApiPresenter::convertToResponse)
                .collect(Collectors.toList());

        return new Pagination<>(
                userListOutput.currentPage(),
                userListOutput.perPage(),
                userListOutput.total(),
                favoriteListResponse
        );
    }

    private static UserListResponse convertToResponse(UserListOutput categoryListOutput) {
        return new UserListResponse(
                categoryListOutput.id(),
                categoryListOutput.name(),
                categoryListOutput.email(),
                categoryListOutput.createdAt()
        );
    }
}
