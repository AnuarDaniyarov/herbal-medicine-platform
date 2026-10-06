package kz.iitu.herbalmedicineplatform.category.dto;

import kz.iitu.herbalmedicineplatform.category.Category;

public record CategoryResponse(Long id, String name, String description) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
