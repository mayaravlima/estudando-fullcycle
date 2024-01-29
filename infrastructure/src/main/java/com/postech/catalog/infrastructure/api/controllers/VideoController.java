package com.postech.catalog.infrastructure.api.controllers;

import com.postech.catalog.application.video.create.CreateVideoCommand;
import com.postech.catalog.application.video.create.CreateVideoOutput;
import com.postech.catalog.application.video.create.CreateVideoUseCase;
import com.postech.catalog.application.video.delete.DeleteVideoUseCase;
import com.postech.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.postech.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.postech.catalog.application.video.update.UpdateVideoCommand;
import com.postech.catalog.application.video.update.UpdateVideoOutput;
import com.postech.catalog.application.video.update.UpdateVideoUseCase;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.validation.handler.Notification;
import com.postech.catalog.domain.video.VideoSearchQuery;
import com.postech.catalog.infrastructure.api.VideoAPI;
import com.postech.catalog.infrastructure.video.models.CreateVideoRequest;
import com.postech.catalog.infrastructure.video.models.UpdateVideoRequest;
import com.postech.catalog.infrastructure.video.models.VideoListResponse;
import com.postech.catalog.infrastructure.video.models.VideoResponse;
import com.postech.catalog.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;
import java.util.function.Function;

import static com.postech.catalog.domain.utils.CollectionUtils.mapTo;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final ListVideosUseCase listVideosUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final ListVideosUseCase listVideosUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase,
            final UpdateVideoUseCase updateVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase) {
        this.createVideoUseCase = createVideoUseCase;
        this.listVideosUseCase = listVideosUseCase;
        this.getVideoByIdUseCase = getVideoByIdUseCase;
        this.updateVideoUseCase = updateVideoUseCase;
        this.deleteVideoUseCase = deleteVideoUseCase;
    }


    @Override
    public Mono<ResponseEntity<?>> createVideo(CreateVideoRequest input) {
        final var command = CreateVideoCommand.with(
                input.title(),
                input.description(),
                input.url(),
                input.categories()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateVideoOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);

        return Mono.just(this.createVideoUseCase.execute(command)
                .fold(onError, onSuccess));
    }

    @Override
    public Flux<Pagination<VideoListResponse>> listVideos(String search, int page, int perPage, String sort, String direction, Set<String> categories) {
        final var categoriesIDs = mapTo(categories, CategoryID::from);

        return Flux.defer(() ->
                Mono.fromCallable(() ->
                                listVideosUseCase.execute(new VideoSearchQuery(page, perPage, search, sort, direction, categoriesIDs))
                        )
                        .map(VideoApiPresenter::present)
                        .flatMapMany(Flux::just)
        );
    }

    @Override
    public Mono<VideoResponse> getById(String id) {
        return Mono.just(VideoApiPresenter.present(this.getVideoByIdUseCase.execute(id)));
    }

    @Override
    public Mono<ResponseEntity<?>> updateById(String id, UpdateVideoRequest input) {
        final var command = UpdateVideoCommand.with(
                id,
                input.title(),
                input.description(),
                input.url(),
                input.categories()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateVideoOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return Mono.just(this.updateVideoUseCase.execute(command)
                .fold(onError, onSuccess));
    }

    @Override
    public void deleteById(String id) {
        this.deleteVideoUseCase.execute(id);
    }
}
