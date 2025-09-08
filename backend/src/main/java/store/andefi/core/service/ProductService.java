package store.andefi.core.service;

import store.andefi.core.entity.Product;
import store.andefi.core.exception.*;
import store.andefi.core.model.ProductCatalogCursorPage;
import store.andefi.core.model.ProductDetails;

import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    ProductCatalogCursorPage getProductCatalog(String category, int limit, String cursor) throws ProductCatalogException;

    ProductDetails getProductDetailsByProductId(String productId) throws ProductDetailsException;

    Optional<Product> getProductByIdOptional(UUID productId);

    ProductCatalogCursorPage semanticSearchProducts(String keyword, int limit, String cursor) throws ProductSearchException, EmbeddingException;

    boolean isProductStockSufficient(UUID productId, Integer quantity) throws ProductStockCheckException;
}
