package com.postech.catalog.infrastructure.user;

import com.postech.catalog.domain.Identifier;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.user.*;
import com.postech.catalog.domain.video.VideoID;
import com.postech.catalog.infrastructure.user.persistence.UserJpaEntity;
import com.postech.catalog.infrastructure.user.persistence.UserRepository;
import com.postech.catalog.infrastructure.utils.SqlUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.postech.catalog.domain.utils.CollectionUtils.mapTo;
import static com.postech.catalog.domain.utils.CollectionUtils.nullIfEmpty;

@Component
public class DefaultUserGateway implements UserGateway {

    private final UserRepository userRepository;

    public DefaultUserGateway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public User create(User aUser) {
        return save(aUser);
    }

    @Override
    @Transactional
    public void deleteById(UserID anId) {
        final var userId = anId.getValue();
        if (this.userRepository.existsById(userId)) {
            this.userRepository.deleteById(userId);
        }
    }

    @Override
    @Transactional
    public User update(User aUser) {
        return save(aUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserID anId) {
        return this.userRepository.findById(anId.getValue())
                .map(UserJpaEntity::toAggregate);
    }

    @Override
    public Pagination<UserPreview> findAll(UserSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.userRepository.findAll(
                SqlUtils.like(SqlUtils.upper(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.favorites(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    @Override
    @Transactional
    public void deleteUserFromUserVideo(UserID id) {
        final var userId = id.getValue();
        this.userRepository.deleteUserFromUserVideo(userId);
    }


    private User save(final User user) {
        return this.userRepository.save(UserJpaEntity.from(user))
                .toAggregate();
    }
}
