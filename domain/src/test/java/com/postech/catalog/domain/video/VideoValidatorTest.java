package com.postech.catalog.domain.video;

import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.exceptions.DomainException;
import com.postech.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        final String expectedTitle = null;
        final var expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "";
        final var expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                """;
        final var expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec euismod, nisl eget
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "Movies";
        final var expectedDescription = "";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "Movies";
        final var expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Pulvinar pellentesque habitant morbi tristique. A condimentum vitae sapien pellentesque. Sociis natoque penatibus et magnis dis parturient montes nascetur ridiculus. Sapien faucibus et molestie ac feugiat sed. Congue quisque egestas diam in arcu cursus euismod quis viverra. Nibh venenatis cras sed felis eget velit aliquet. Sed id semper risus in hendrerit gravida rutrum quisque. Odio morbi quis commodo odio aenean sed. Eu non diam phasellus vestibulum lorem sed risus ultricies. Elementum pulvinar etiam non quam. Nulla malesuada pellentesque elit eget gravida cum sociis natoque penatibus. Dictum fusce ut placerat orci nulla pellentesque dignissim enim. Convallis posuere morbi leo urna molestie at. Eu scelerisque felis imperdiet proin fermentum leo vel orci porta. Sit amet cursus sit amet dictum sit.
                                
                Tellus in hac habitasse platea. Vitae suscipit tellus mauris a diam maecenas sed. Tincidunt vitae semper quis lectus. Viverra tellus in hac habitasse platea dictumst vestibulum rhoncus. Duis at consectetur lorem donec massa sapien faucibus et molestie. A scelerisque purus semper eget duis at tellus at. Blandit cursus risus at ultrices mi tempus imperdiet nulla. Nibh mauris cursus mattis molestie a. Amet porttitor eget dolor morbi non arcu risus quis varius. Dolor morbi non arcu risus quis varius.
                                
                Consectetur adipiscing elit pellentesque habitant. Sed viverra tellus in hac habitasse platea dictumst. Eu augue ut lectus arcu bibendum at varius. Nunc pulvinar sapien et ligula ullamcorper malesuada proin libero nunc. Venenatis tellus in metus vulputate eu. Integer eget aliquet nibh praesent tristique. Elementum sagittis vitae et leo duis ut diam quam. Aenean et tortor at risus viverra adipiscing at in tellus. Arcu odio ut sem nulla. Justo eget magna fermentum iaculis eu non diam phasellus. At in tellus integer feugiat scelerisque varius. Purus ut faucibus pulvinar elementum. Sed turpis tincidunt id aliquet risus. Aliquet porttitor lacus luctus accumsan tortor posuere ac. Mi tempus imperdiet nulla malesuada pellentesque elit eget gravida. In nulla posuere sollicitudin aliquam.
                                
                Neque gravida in fermentum et sollicitudin. Erat velit scelerisque in dictum non consectetur a erat. Condimentum mattis pellentesque id nibh tortor id aliquet lectus proin. Nunc lobortis mattis aliquam faucibus purus. Maecenas sed enim ut sem viverra aliquet eget. Erat velit scelerisque in dictum non consectetur. In dictum non consectetur a erat nam. Quam nulla porttitor massa id neque. Lorem ipsum dolor sit amet consectetur adipiscing. Placerat orci nulla pellentesque dignissim enim sit. Amet nisl purus in mollis nunc sed. Nunc faucibus a pellentesque sit amet porttitor eget dolor.
                                
                Imperdiet nulla malesuada pellentesque elit eget gravida cum sociis. Pretium vulputate sapien nec sagittis aliquam. Pellentesque elit ullamcorper dignissim cras tincidunt lobortis feugiat. Quis blandit turpis cursus in hac habitasse platea dictumst quisque. Ornare aenean euismod elementum nisi. Rhoncus mattis rhoncus urna neque. Nulla malesuada pellentesque elit eget gravida cum sociis. Ut venenatis tellus in metus vulputate. Pharetra diam sit amet nisl suscipit. Malesuada bibendum arcu vitae elementum curabitur vitae nunc sed velit. Senectus et netus et malesuada fames ac turpis egestas integer. Ultricies leo integer malesuada nunc vel risus commodo. In metus vulputate eu scelerisque. Vel risus commodo viverra maecenas accumsan. Quisque sagittis purus sit amet volutpat consequat mauris.
                                
                Sem et tortor consequat id porta. Tellus cras adipiscing enim eu. Netus et malesuada fames ac. Vulputate eu scelerisque felis imperdiet proin. Laoreet id donec ultrices tincidunt arcu. Vel quam elementum pulvinar etiam non quam. Vitae elementum curabitur vitae nunc sed. Elit ut aliquam purus sit amet luctus. Mattis pellentesque id nibh tortor. Mollis nunc sed id semper. Nunc congue nisi vitae suscipit tellus. Magna fermentum iaculis eu non diam phasellus vestibulum lorem. Vitae nunc sed velit dignissim. Aenean euismod elementum nisi quis eleifend quam adipiscing vitae. Mollis aliquam ut porttitor leo a diam. Pulvinar sapien et ligula ullamcorper malesuada proin libero nunc consequat. Adipiscing elit ut aliquam purus sit amet luctus. Ut tortor pretium viverra suspendisse potenti nullam.
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "Movies";
        final var expectedDescription = "A description";
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "Movies";
        final var expectedDescription = "A description";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories
        );

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
}
