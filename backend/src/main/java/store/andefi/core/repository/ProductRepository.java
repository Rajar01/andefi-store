package store.andefi.core.repository;

import store.andefi.core.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findProductByCategory(String category, int limit, String cursor);

    List<Product> findAllProducts(int limit, String cursor);

    Optional<Product> findProductByIdOptional(UUID id);

    List<Product> semanticSearch(float[] keyword, int limit, String cursor);

    Double calculateProductEmbeddingL2Distance(float[] embedding, UUID productId);
}
