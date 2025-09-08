package store.andefi.infrastructure.persistence.panache.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.core.entity.Category;
import store.andefi.core.entity.Product;
import store.andefi.core.entity.Stock;
import store.andefi.core.repository.ProductRepository;
import store.andefi.infrastructure.persistence.panache.entity.ProductPanacheEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRepositoryImpl implements ProductRepository, PanacheRepositoryBase<ProductPanacheEntity, UUID> {
    // Convert panache entity into core entity (domain entity)
    private Product toDomainEntity(ProductPanacheEntity productPanacheEntity) {
        Stock stock = Stock.builder()
            .id(productPanacheEntity.getStock().getId())
            .quantityOnHand(productPanacheEntity.getStock().getQuantityOnHand())
            .availableQuantity(productPanacheEntity.getStock().getAvailableQuantity())
            .reservedQuantity(productPanacheEntity.getStock().getReservedQuantity())
            .soldQuantity(productPanacheEntity.getStock().getSoldQuantity())
            .build();

        Set<Category> categories = productPanacheEntity.getCategories().stream()
            .map(it -> Category.builder()
                .id(it.getId())
                .name(it.getName())
                .build()
            ).collect(Collectors.toSet());

        return Product.builder()
            .id(productPanacheEntity.getId())
            .name(productPanacheEntity.getName())
            .description(productPanacheEntity.getDescription())
            .price(productPanacheEntity.getPrice())
            .discountPercentage(productPanacheEntity.getDiscountPercentage())
            .attributes(productPanacheEntity.getAttributes())
            .mediaUrls(productPanacheEntity.getMediaUrls())
            .stock(stock)
            .categories(categories)
            .build();
    }

    @Override
    public List<Product> findAllProducts(int limit, String cursor) {
        List<ProductPanacheEntity> productPanacheEntities;

        if (cursor.isBlank()) {
            productPanacheEntities = findAll(Sort.by("id", Sort.Direction.Ascending))
                .page(0, limit + 1)
                .list();
        } else {
            productPanacheEntities = find("id >= ?1", Sort.by("id", Sort.Direction.Ascending), UUID.fromString(cursor))
                .page(0, limit + 1)
                .list();
        }

        return productPanacheEntities.stream().map(this::toDomainEntity).toList();
    }

    @Override
    public Optional<Product> findProductByIdOptional(UUID id) {
        return findByIdOptional(id).map(this::toDomainEntity);
    }

    @Override
    public List<Product> findProductByCategory(String category, int limit, String cursor) {
        List<ProductPanacheEntity> productPanacheEntities;

        if (cursor.isBlank()) {
            productPanacheEntities = find("SELECT p FROM ProductPanacheEntity p JOIN p.categories c WHERE c.name = ?1",
                Sort.by("p.id", Sort.Direction.Ascending),
                category
            ).page(0, limit + 1).list();
        } else {
            productPanacheEntities = find("SELECT p FROM ProductPanacheEntity p JOIN p.categories c WHERE p.id >= ?1 and c.name = ?2",
                Sort.by("p.id", Sort.Direction.Ascending),
                UUID.fromString(cursor),
                category
            ).page(0, limit + 1).list();
        }

        return productPanacheEntities.stream().map(this::toDomainEntity).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Product> semanticSearch(float[] keyword, int limit, String cursor) {
        List<ProductPanacheEntity> productPanacheEntities;

        if (cursor == null || cursor.isBlank()) {
            productPanacheEntities = getEntityManager().createNativeQuery(
                "SELECT p.* FROM products p ORDER BY embedding <-> CAST(:embedding AS vector), p.id LIMIT :limit",
                ProductPanacheEntity.class
            ).setParameter("embedding", keyword).setParameter("limit", limit + 1).getResultList();
        } else {
            // Get last product id and last distance
            String[] parts = cursor.split(",");

            productPanacheEntities = getEntityManager().createNativeQuery(
                "SELECT p.* FROM products p WHERE (embedding <-> CAST(:embedding AS vector)) > :last_distance OR ((embedding <-> CAST(:embedding AS vector)) = :last_distance AND p.id >= :last_product_id) ORDER BY embedding <-> CAST(:embedding AS vector), p.id LIMIT :limit",
                ProductPanacheEntity.class
            ).setParameter("embedding", keyword).setParameter("limit", limit + 1).setParameter("last_product_id", UUID.fromString(parts[0])).setParameter("last_distance", Double.parseDouble(parts[1])).getResultList();
        }

        return productPanacheEntities.stream().map(this::toDomainEntity).toList();
    }

    @Override
    public Double calculateProductEmbeddingL2Distance(float[] embedding, UUID productId) {
        return (Double) getEntityManager()
            .createNativeQuery(
                "SELECT embedding <-> CAST(:embedding AS vector) FROM products WHERE id = :product_id")
            .setParameter("embedding", embedding)
            .setParameter("product_id", productId)
            .getSingleResult();
    }
}
