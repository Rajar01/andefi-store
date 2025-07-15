package store.andefi.utility.mapper;

import store.andefi.dto.ProductDto;
import store.andefi.entity.Category;
import store.andefi.entity.Product;

import java.util.List;

public final class ProductMapper {
    public static ProductDto toDto(Product product) {
        List<String> categories = null;

        if (product.categories() != null) {
            categories = product.categories().stream()
                    .map(Category::name).toList();
        }

        return new ProductDto(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                categories,
                product.attributes()
        );
    }
}
