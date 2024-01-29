package com.postech.catalog.infrastructure.configuration;

import com.postech.catalog.application.video.create.CreateVideoUseCase;
import com.postech.catalog.application.video.create.DefaultCreateVideoUseCase;
import com.postech.catalog.application.video.delete.DefaultDeleteVideoUseCase;
import com.postech.catalog.application.video.delete.DeleteVideoUseCase;
import com.postech.catalog.application.video.metrics.DefaultGetVideoMetricsUseCase;
import com.postech.catalog.application.video.metrics.GetVideoMetricsUseCase;
import com.postech.catalog.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.postech.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.postech.catalog.application.video.retrieve.list.DefaultListVideosUseCase;
import com.postech.catalog.application.video.retrieve.list.ListVideosUseCase;
import com.postech.catalog.application.video.update.DefaultUpdateVideoUseCase;
import com.postech.catalog.application.video.update.UpdateVideoUseCase;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(
            final CategoryGateway categoryGateway,
            final VideoGateway videoGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, videoGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(videoGateway, categoryGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }

    @Bean
    public GetVideoMetricsUseCase getVideoMetricsUseCase() {
        return new DefaultGetVideoMetricsUseCase(videoGateway);
    }
}
