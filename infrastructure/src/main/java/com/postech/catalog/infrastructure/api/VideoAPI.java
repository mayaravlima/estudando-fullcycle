package com.postech.catalog.infrastructure.api;

import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.infrastructure.video.models.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@RequestMapping(value = "videos")
@Tag(name = "Videos")
public interface VideoAPI {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new video")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Mono<ResponseEntity<?>> createVideo(@RequestBody CreateVideoRequest input);

    @GetMapping
    @Operation(summary = "List all videos paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Flux<Pagination<VideoListResponse>> listVideos(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "createdAt") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "desc") final String direction,
            @RequestParam(name = "categories_ids", required = false, defaultValue = "") Set<String> categories
    );

   @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a video by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Video was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Mono<VideoResponse> getById(@PathVariable(name = "id") String id);


    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a video by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video updated successfully"),
            @ApiResponse(responseCode = "404", description = "Video was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Mono<ResponseEntity<?>> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateVideoRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a video by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Videos deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Videos was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable(name = "id") String id);


    @GetMapping(
            value = "metrics",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get videos metrics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video Metrics retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Video Metrics was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Mono<VideoMetricsResponse> getMetrics();

}
