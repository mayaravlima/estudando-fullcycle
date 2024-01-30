package com.postech.catalog.domain.user;

import com.postech.catalog.domain.exceptions.DomainException;
import com.postech.catalog.domain.validation.handler.ThrowsValidationHandler;
import com.postech.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class UserValidatorTest {

    @Test
    public void givenNullName_whenCallsValidate_shouldReceiveError() {
        final String expectedName = null;
        final var expectedEmail = "test@email.com";
        final var expectedFavorites = Set.of(VideoID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualUser = User.newUser(
                expectedName,
                expectedEmail,
                expectedFavorites
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualUser.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyName_whenCallsValidate_shouldReceiveError() {
        final String expectedName = "";
        final var expectedEmail = "test@email.com";
        final var expectedFavorites = Set.of(VideoID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualUser = User.newUser(
                expectedName,
                expectedEmail,
                expectedFavorites
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualUser.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNameWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        final String expectedName = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                """;
        final var expectedEmail = "test@email.com";
        final var expectedFavorites = Set.of(VideoID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualUser = User.newUser(
                expectedName,
                expectedEmail,
                expectedFavorites
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualUser.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullEmail_whenCallsValidate_shouldReceiveError() {
        final String expectedName = "Test";
        final String expectedEmail = null;
        final var expectedFavorites = Set.of(VideoID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'email' should not be null";

        final var actualUser = User.newUser(
                expectedName,
                expectedEmail,
                expectedFavorites
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualUser.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyEmail_whenCallsValidate_shouldReceiveError() {
        final String expectedName = "Test";
        final var expectedEmail = "";
        final var expectedFavorites = Set.of(VideoID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'email' should not be empty";

        final var actualUser = User.newUser(
                expectedName,
                expectedEmail,
                expectedFavorites
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualUser.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmailWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        final String expectedEmail = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                """;
        final var expectedName = "Test";
        final var expectedFavorites = Set.of(VideoID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'email' must be between 1 and 255 characters";

        final var actualUser = User.newUser(
                expectedName,
                expectedEmail,
                expectedFavorites
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualUser.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
}
