package com.postech.catalog.application.user.retrieve.list;

import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.NotFoundException;
import com.postech.catalog.domain.user.User;
import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.user.UserID;
import com.postech.catalog.domain.video.VideoGateway;
import com.postech.catalog.domain.video.VideoSearchQuery;

import java.util.*;

public class DefaultListRecommendationsUseCase extends
        ListRecommendationsUseCase {

    private final UserGateway userGateway;
    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;

    public DefaultListRecommendationsUseCase(
            final UserGateway userGateway,
            final VideoGateway videoGateway,
            final CategoryGateway categoryGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Map<String, UserRecommendationOutput> execute(String input) {
        final var userRecommendations = new HashMap<String, UserRecommendationOutput>();
        final var userId = UserID.from(input);

        final var users = userGateway.findById(userId)
                .orElseThrow(() -> NotFoundException.with(User.class, userId));

        users.getFavorites().stream().forEach(favorite -> {
            final var video = videoGateway.findById(favorite).get();
            final var categoriesFromVideo = new ArrayList<CategoryID>(video.getCategories());
            final var category = categoryGateway.findById(categoriesFromVideo.get(0)).get();

            if (!userRecommendations.containsKey(category.getName())) {
                final var videoSearchQuery = new VideoSearchQuery(0, 5, "", "createdAt", "desc", Set.of(category.getId()));

                final var videosPage = videoGateway.findAll(videoSearchQuery);

                userRecommendations.put(category.getName(), new UserRecommendationOutput(videosPage.items()));
            }
        });


        return userRecommendations;
    }
}
