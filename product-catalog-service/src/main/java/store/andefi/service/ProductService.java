package store.andefi.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import store.andefi.dto.ProductCatalogDto;
import store.andefi.dto.ProductDto;
import store.andefi.repository.ProductRepository;
import store.andefi.utility.CursorCodec;
import store.andefi.utility.mapper.ProductMapper;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    public ProductCatalogDto getProductCatalog(int limit, String cursor) {
        List<ProductDto> products;

        if (limit < 1 || limit > 100) {
            throw new BadRequestException();
        }

        if (cursor == null || cursor.isBlank()) {
            products =
                    productRepository
                            .findAll(Sort.by("_id", Sort.Direction.Ascending))
                            .page(0, limit + 1)
                            .stream().map(ProductMapper::toDto).toList();
        } else {
            UUID c = UUID.fromString(CursorCodec.decode(cursor));

            products =
                    productRepository
                            .find("_id >= ?1", Sort.by("_id", Sort.Direction.Ascending), c)
                            .page(0, limit + 1)
                            .stream().map(ProductMapper::toDto).toList();
        }

        boolean hasMore = products.size() > limit;
        String nextCursor = hasMore ? CursorCodec.encode(products.getLast().id().toString()) : null;
        if (hasMore) products = products.subList(0, products.size() - 1);

        return new ProductCatalogDto(products, hasMore, nextCursor);
    }

    public ProductDto getProductById(UUID productId) {
        return productRepository.findByIdOptional(productId).map(ProductMapper::toDto).orElseThrow(NotFoundException::new);
    }
}
