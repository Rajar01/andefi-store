package store.andefi.utility.mapper;

import store.andefi.dto.CategoryDto;
import store.andefi.entity.Category;

public final class CategoryMapper {
  public static CategoryDto toDto(Category categoryEntity) {
    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setId(categoryEntity.getId().toString());
    categoryDto.setName(categoryEntity.getName());

    return categoryDto;
  }
}
