package com.postech.catalog.application.category.update;

public record UpdateCategoryCommand(
        String id,
        String name,
        String description,
        Boolean isActive
) {

    public static UpdateCategoryCommand with(
            final String id,
            final String name,
            final String description,
            final Boolean isActive) {
        return new UpdateCategoryCommand(id, name, description, isActive);
    }
}
