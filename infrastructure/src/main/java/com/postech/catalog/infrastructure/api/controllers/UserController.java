package com.postech.catalog.infrastructure.api.controllers;

import com.postech.catalog.application.user.create.CreateUserCommand;
import com.postech.catalog.application.user.create.CreateUserOutput;
import com.postech.catalog.application.user.create.CreateUserUseCase;
import com.postech.catalog.application.user.delete.DeleteUserUseCase;
import com.postech.catalog.application.user.retrieve.get.GetUserByIdUseCase;
import com.postech.catalog.application.user.retrieve.list.ListRecommendationsUseCase;
import com.postech.catalog.application.user.retrieve.list.ListUsersUseCase;
import com.postech.catalog.application.user.update.UpdateUserCommand;
import com.postech.catalog.application.user.update.UpdateUserOutput;
import com.postech.catalog.application.user.update.UpdateUserUseCase;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.user.UserSearchQuery;
import com.postech.catalog.domain.validation.handler.Notification;
import com.postech.catalog.domain.video.VideoID;
import com.postech.catalog.infrastructure.api.UserAPI;
import com.postech.catalog.infrastructure.user.models.*;
import com.postech.catalog.infrastructure.user.presenters.UserApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.postech.catalog.domain.utils.CollectionUtils.mapTo;

@RestController
public class UserController implements UserAPI {

    private final CreateUserUseCase createUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ListRecommendationsUseCase listRecommendationsUseCase;

    public UserController(
            final CreateUserUseCase createUserUseCase,
            final ListUsersUseCase listUsersUseCase,
            final GetUserByIdUseCase getUserByIdUseCase,
            final UpdateUserUseCase updateUserUseCase,
            final DeleteUserUseCase deleteUserUseCase,
            final ListRecommendationsUseCase listRecommendationsUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.listRecommendationsUseCase = listRecommendationsUseCase;
    }

    @Override
    public Flux<List<UserRecommendationResponse>> getRecommendations(String id)  {
        return Flux.defer(() ->
                Mono.fromCallable(() -> listRecommendationsUseCase.execute(id))
                        .map(UserApiPresenter::present)
                        .flatMapMany(Flux::just)
        );
    }

    @Override
    public Mono<ResponseEntity<?>> createVideo(CreateUserRequest input) {
        final var command = CreateUserCommand.with(
                input.name(),
                input.email(),
                input.favorites()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateUserOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/users/" + output.id())).body(output);

        return Mono.just(this.createUserUseCase.execute(command)
                .fold(onError, onSuccess));
    }

    @Override
    public Flux<Pagination<UserListResponse>> listVideos(String search, int page, int perPage, String sort, String direction, Set<String> favorites) {
        final var videosIds = mapTo(favorites, VideoID::from);

        return Flux.defer(() ->
                Mono.fromCallable(() ->
                                listUsersUseCase.execute(new UserSearchQuery(page, perPage, search, sort, direction, videosIds))
                        )
                        .map(UserApiPresenter::present)
                        .flatMapMany(Flux::just)
        );
    }

    @Override
    public Mono<UserResponse> getById(String id) {
        return Mono.just(UserApiPresenter.present(this.getUserByIdUseCase.execute(id)));
    }

    @Override
    public Mono<ResponseEntity<?>> updateById(String id, UpdateUserRequest input) {
        final var command = UpdateUserCommand.with(
                id,
                input.name(),
                input.email(),
                input.favorites()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateUserOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return Mono.just(this.updateUserUseCase.execute(command)
                .fold(onError, onSuccess));
    }

    @Override
    public void deleteById(String id) {
        this.deleteUserUseCase.execute(id);
    }
}
