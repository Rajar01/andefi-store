package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import store.andefi.dto.CategoryDto;
import store.andefi.repository.CategoryRepository;
import store.andefi.utility.mapper.CategoryMapper;

import java.util.List;

@ApplicationScoped
public class CategoryService {
    @Inject
    CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(CategoryMapper::toDto).toList();
    }
}
