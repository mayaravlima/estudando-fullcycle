package com.postech.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.catalog.ControllerTest;
import com.postech.catalog.application.category.update.UpdateCategoryOutput;
import com.postech.catalog.application.video.create.CreateVideoOutput;
import com.postech.catalog.application.video.create.CreateVideoUseCase;
import com.postech.catalog.application.video.delete.DeleteVideoUseCase;
import com.postech.catalog.application.video.metrics.GetVideoMetricsUseCase;
import com.postech.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.postech.catalog.application.video.retrieve.get.VideoOutput;
import com.postech.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.postech.catalog.application.video.retrieve.list.VideoListOutput;
import com.postech.catalog.application.video.update.UpdateVideoOutput;
import com.postech.catalog.application.video.update.UpdateVideoUseCase;
import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.exceptions.NotFoundException;
import com.postech.catalog.domain.exceptions.NotificationException;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.domain.utils.InstantUtils;
import com.postech.catalog.domain.validation.handler.Notification;
import com.postech.catalog.domain.video.Video;
import com.postech.catalog.domain.video.VideoID;
import com.postech.catalog.domain.video.VideoPreview;
import com.postech.catalog.infrastructure.video.models.CreateVideoRequest;
import com.postech.catalog.infrastructure.video.models.UpdateVideoRequest;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ControllerTest(controllers = VideoAPI.class)
public class VideoAPITest {

    @Autowired
    private WebTestClient mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private ListVideosUseCase listVideosUseCase;

    @MockBean
    private GetVideoMetricsUseCase getVideoMetricsUseCase;

    @Test
    public void givenValidCommand_whenCallingCreateFull_shouldReturnId() throws Exception {
        final var categoryOne = Category.newCategory("Movies", null, true);
        final var categoryTwo = Category.newCategory("Comedy", null, true);

        final var expectedId = VideoID.unique();
        final var expectedTitle = "Title";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=123";
        final var expectedClickCount = 10L;
        final var expectedCategories = Set.of(categoryOne.getId().toString(), categoryTwo.getId().toString());

        when(createVideoUseCase.execute(any()))
                .thenReturn(Either.right(new CreateVideoOutput(expectedId.getValue())));

        final var input = new CreateVideoRequest(expectedTitle, expectedDescription, expectedUrl, expectedClickCount, expectedCategories);

        this.mvc.post().uri(uriBuilder -> uriBuilder.path("/videos").build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(this.mapper.writeValueAsString(input))
                .exchange().expectStatus().isCreated().expectBody()
                .jsonPath("$.id").isEqualTo(expectedId.getValue());

        verify(createVideoUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedTitle, cmd.title())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectedUrl, cmd.url())
                && Objects.equals(expectedClickCount, cmd.clickCount())
                && Objects.equals(expectedCategories, cmd.categories())
        ));
    }

    @Test
    public void givenEmptyBody_whenCallingCreateFull_shouldReturnBadRequest() throws Exception {
        this.mvc.post().uri(uriBuilder -> uriBuilder.path("/videos").build())
              .contentType(MediaType.APPLICATION_JSON)
              .exchange().expectStatus().isBadRequest();
    }

    @Test
    public void givenValidId_whenCallingGetById_shouldReturnVideo() throws Exception {
        final var categoryOne = Category.newCategory("Movies", null, true);
        final var categoryTwo = Category.newCategory("Comedy", null, true);


        final var expectedTitle = "Title";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=123";
        final var expectedClickCount = 10L;
        final var expectedCategories = Set.of(categoryOne.getId(), categoryTwo.getId());

        final var video = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedUrl,
                expectedClickCount,
                expectedCategories
        );

        final var expectedId = video.getId().getValue();

        when(getVideoByIdUseCase.execute(any())).thenReturn(VideoOutput.from(video));

        this.mvc.get().uri(uriBuilder -> uriBuilder.path("/videos/{id}").build(expectedId))
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.id").isEqualTo(expectedId)
                .jsonPath("$.title").isEqualTo(video.getTitle())
                .jsonPath("$.description").isEqualTo(video.getDescription())
                .jsonPath("$.url").isEqualTo(video.getUrl())
                .jsonPath("$.click_count").isEqualTo(video.getClickCount())
                .jsonPath("$.categories_id[0]").isNotEmpty()
                .jsonPath("$.categories_id[1]").isNotEmpty();

        verify(getVideoByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallingGetById_shouldReturnNotFound() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(getVideoByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Video.class, expectedId));

        this.mvc.get().uri(uriBuilder -> uriBuilder.path("/videos/{id}").build(expectedId))
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNotFound().expectBody()
                .jsonPath("$.message").isEqualTo(expectedErrorMessage);
    }

    @Test
    public void givenValidCommand_whenCallingUpdateVideo_shouldReturnVideoId() throws Exception {
        final var categoryOne = Category.newCategory("Movies", null, true);
        final var categoryTwo = Category.newCategory("Comedy", null, true);

        final var expectedId = VideoID.unique();
        final var expectedTitle = "Title";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=123";
        final var expectedClickCount = 10L;
        final var expectedCategories = Set.of(categoryOne.getId().toString(), categoryTwo.getId().toString());

        final var command = new UpdateVideoRequest(
                expectedId.getValue(), expectedTitle, expectedDescription,
                expectedUrl, expectedClickCount, expectedCategories);

        when(updateVideoUseCase.execute(any()))
                .thenReturn(Either.right(new UpdateVideoOutput(expectedId.getValue())));

        final var input = new UpdateVideoRequest(expectedId.getValue(),
                expectedTitle, expectedDescription, expectedUrl, expectedClickCount, expectedCategories);

        this.mvc.put().uri(uriBuilder -> uriBuilder.path("/videos/{id}").build(1234))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(this.mapper.writeValueAsString(input))
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("$.id").isEqualTo(expectedId.getValue());
    }

    @Test
    public void givenInvalidCommand_whenCallingUpdateVideo_shouldReturnNotification() throws Exception {
        final var categoryOne = Category.newCategory("Movies", null, true);
        final var categoryTwo = Category.newCategory("Comedy", null, true);

        final var expectedId = VideoID.unique();
        final var expectedTitle = "";
        final var expectedMessage = "'title' should not be empty";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=123";
        final var expectedClickCount = 10L;
        final var expectedCategories = Set.of(categoryOne.getId().toString(), categoryTwo.getId().toString());

        final var command = new UpdateVideoRequest(
                expectedId.getValue(), expectedTitle, expectedDescription,
                expectedUrl, expectedClickCount, expectedCategories);

        when(updateVideoUseCase.execute(any()))
                .thenReturn(Either.left(Notification.create(new Error(expectedMessage))));

        final var input = new UpdateVideoRequest(expectedId.getValue(),
                expectedTitle, expectedDescription, expectedUrl, expectedClickCount, expectedCategories);

        this.mvc.put().uri(uriBuilder -> uriBuilder.path("/videos/{id}").build(1234))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(this.mapper.writeValueAsString(input))
                .exchange().expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody().jsonPath("$.errors[0].message").isEqualTo(expectedMessage)
                .jsonPath("$.errors", hasSize(1));

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    public void givenInvalidId_whenCallingUpdateVideo_shouldReturnNotFound() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(updateVideoUseCase.execute(any()))
              .thenThrow(NotFoundException.with(Video.class, expectedId));

        this.mvc.put().uri(uriBuilder -> uriBuilder.path("/videos/{id}").build(expectedId))
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(new UpdateVideoRequest(expectedId.getValue(), "Title", "Description", "Url", 10L, Set.of()))
              .exchange().expectStatus().isNotFound().expectBody()
              .jsonPath("$.message").isEqualTo(expectedErrorMessage);

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    public void givenValidCommand_whenCallingDeleteVideo_shouldDeleteIt() throws Exception {
        final var expectedId = VideoID.unique();

        doNothing().when(deleteVideoUseCase).execute(any());

        this.mvc.delete().uri(uriBuilder -> uriBuilder.path("/videos/{id}").build(expectedId.getValue()))
               .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNoContent();

        verify(deleteVideoUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidParams_whenCallingListVideos_shouldReturnPagination() throws Exception {
        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Test";
        final var expectedSortBy = "title";
        final var expectedSortOrder = "desc";
        final var expectedCategories = Set.of("Movies");

        final var expectedId = VideoID.unique();
        final var expectedTitle = "Title";
        final var expectedDescription = "Description";
        final var expectedUrl = "https://www.youtube.com/watch?v=123";
        final var expectedClickCount = 10L;

        final var videoPreview = new VideoPreview(expectedId.getValue(), expectedTitle,
                expectedUrl, expectedDescription, expectedClickCount, InstantUtils.now());

        final var expectedItems = List.of(VideoListOutput.from(videoPreview));

        when(listVideosUseCase.execute(any())).thenReturn(new Pagination<>
                (expectedPage, expectedPerPage, 1, expectedItems));

        this.mvc.get().uri(uriBuilder -> uriBuilder.path("/videos")
                        .queryParam("page", String.valueOf(expectedPage))
                        .queryParam("perPage", String.valueOf(expectedPerPage))
                        .queryParam("sort", expectedSortBy)
                        .queryParam("dir", expectedSortOrder)
                        .queryParam("search", expectedTerms)
                        .queryParam("categories_ids", expectedCategories)
                        .build())
             .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody()
                .jsonPath("$[0].current_page").isEqualTo(expectedPage)
                .jsonPath("$[0].per_page").isEqualTo(expectedPerPage)
                .jsonPath("$[0].total").isEqualTo(1)
                .jsonPath("$[0].items[0].title").isEqualTo(videoPreview.title())
                .jsonPath("$[0].items[0].description").isEqualTo(videoPreview.description())
                .jsonPath("$[0].items[0].created_at").isEqualTo(videoPreview.createdAt().toString());

        verify(listVideosUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedSortOrder, query.direction())
                        && Objects.equals(expectedSortBy, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
