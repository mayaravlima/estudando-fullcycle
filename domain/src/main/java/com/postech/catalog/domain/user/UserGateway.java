package com.postech.catalog.domain.user;

import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.video.VideoID;

import java.util.List;
import java.util.Optional;

public interface UserGateway {

        User create(User aUser);

        void deleteById(UserID anId);

        User update(User aUser);

        Optional<User> findById(UserID anId);

        Pagination<UserPreview> findAll(UserSearchQuery aQuery);

        void deleteUserFromUserVideo(UserID userId);

}
