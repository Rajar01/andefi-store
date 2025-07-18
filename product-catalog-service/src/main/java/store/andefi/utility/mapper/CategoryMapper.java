package store.andefi.utility.mapper;

import store.andefi.dto.CategoryDto;
import store.andefi.entity.Category;

public final class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.id().toString(), category.name());
    }
}
