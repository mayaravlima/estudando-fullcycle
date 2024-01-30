package com.postech.catalog.domain.user;

import com.postech.catalog.domain.validation.handler.ThrowsValidationHandler;
import com.postech.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void givenValidParams_whenCallsNewUser_shouldInstantiate() {
        final var expectedName = "Test user";
        final var expectedEmail = "test@email.com";
        final var expectedFavorites = Set.of(VideoID.unique());

        final var actualUser = User.newUser(
                expectedName,
                expectedEmail,
                expectedFavorites
        );

        assertNotNull(actualUser);
        assertNotNull(actualUser.getId());
        assertNotNull(actualUser.getCreatedAt());
        assertEquals(expectedEmail, actualUser.getEmail());
        assertEquals(expectedName, actualUser.getName());
        assertEquals(expectedFavorites, actualUser.getFavorites());


        assertDoesNotThrow(() -> actualUser.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidUser_whenCallsUpdate_shouldReturnUpdated() throws InterruptedException {
        final var expectedName = "Test user";
        final var expectedEmail = "test@email.com";
        final var expectedFavorites = Set.of(VideoID.unique());

        final var user = User.newUser(
                expectedName,
                expectedEmail,
                expectedFavorites
        );

        Thread.sleep(10);

        final var actualUser = User.with(
                user.getId(),
                expectedName,
                expectedEmail,
                user.getCreatedAt(),
                expectedFavorites
        ).update(
                expectedName,
                expectedEmail,
                expectedFavorites
        );


        assertNotNull(actualUser);
        assertNotNull(actualUser.getId());
        assertEquals(user.getCreatedAt(), actualUser.getCreatedAt());
        assertEquals(expectedName, actualUser.getName());
        assertEquals(expectedEmail, actualUser.getEmail());
        assertEquals(expectedFavorites, actualUser.getFavorites());

        assertDoesNotThrow(() -> actualUser.validate(new ThrowsValidationHandler()));
    }

}
