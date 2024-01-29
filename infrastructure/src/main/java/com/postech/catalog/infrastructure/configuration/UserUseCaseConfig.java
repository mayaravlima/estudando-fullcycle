package com.postech.catalog.infrastructure.configuration;

import com.postech.catalog.application.user.create.CreateUserUseCase;
import com.postech.catalog.application.user.create.DefaultCreateUserUseCase;
import com.postech.catalog.application.user.delete.DefaultDeleteUserUseCase;
import com.postech.catalog.application.user.delete.DeleteUserUseCase;
import com.postech.catalog.application.user.retrieve.get.DefaultGetUserByIdUseCase;
import com.postech.catalog.application.user.retrieve.get.GetUserByIdUseCase;
import com.postech.catalog.application.user.retrieve.list.DefaultListUserUseCase;
import com.postech.catalog.application.user.retrieve.list.ListUsersUseCase;
import com.postech.catalog.application.user.update.DefaultUpdateUserUseCase;
import com.postech.catalog.application.user.update.UpdateUserUseCase;
import com.postech.catalog.domain.user.UserGateway;
import com.postech.catalog.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

    private final UserGateway userGateway;

    private final VideoGateway videoGateway;

    public UserUseCaseConfig(
            final UserGateway userGateway,
            final VideoGateway videoGateway) {
        this.userGateway = userGateway;
        this.videoGateway = videoGateway;
    }

    @Bean
    public CreateUserUseCase createUserUseCase() {
        return new DefaultCreateUserUseCase(userGateway, videoGateway);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase() {
        return new DefaultUpdateUserUseCase(userGateway, videoGateway);
    }

    @Bean
    public GetUserByIdUseCase getUserByIdUseCase() {
        return new DefaultGetUserByIdUseCase(userGateway);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase() {
        return new DefaultDeleteUserUseCase(userGateway);
    }

    @Bean
    public ListUsersUseCase listUsersUseCase() {
        return new DefaultListUserUseCase(userGateway);
    }
}
