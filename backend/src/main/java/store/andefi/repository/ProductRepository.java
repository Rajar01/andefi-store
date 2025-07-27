package store.andefi.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import java.util.List;
import java.util.UUID;
import store.andefi.dto.ProductDto;
import store.andefi.dto.ProductsDto;
import store.andefi.entity.Product;
import store.andefi.utility.CursorCodec;
import store.andefi.utility.mapper.ProductMapper;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<Product, UUID> {
  @SuppressWarnings("unchecked")
  public ProductsDto semanticSearch(float[] embeddingKeyword, int limit, String cursor) {
    List<Product> productEntities;

    if (limit < 1 || limit > 100) {
      throw new BadRequestException();
    }

    if (cursor == null || cursor.isBlank()) {
      productEntities =
          getEntityManager()
              .createNativeQuery(
                  "SELECT p.* FROM products p ORDER BY embedding <-> CAST(:embedding AS vector), p.id LIMIT :limit",
                  Product.class)
              .setParameter("embedding", embeddingKeyword)
              .setParameter("limit", limit + 1)
              .getResultList();
    } else {
      UUID c = UUID.fromString(CursorCodec.decode(cursor).split(",")[0]);
      Double d = Double.parseDouble(CursorCodec.decode(cursor).split(",")[1]);

      productEntities =
          getEntityManager()
              .createNativeQuery(
                  "SELECT p.* FROM products p WHERE (embedding <-> CAST(:embedding AS vector)) > :lastDistance OR ((embedding <-> CAST(:embedding AS vector)) = :lastDistance AND p.id >= :lastId) ORDER BY embedding <-> CAST(:embedding AS vector), p.id LIMIT :limit",
                  Product.class)
              .setParameter("embedding", embeddingKeyword)
              .setParameter("limit", limit + 1)
              .setParameter("lastId", c)
              .setParameter("lastDistance", d)
              .getResultList();
    }

    // Calculate distance for the last product
    Double distance =
        (Double)
            getEntityManager()
                .createNativeQuery(
                    "SELECT embedding <-> CAST(:embedding AS vector) FROM products WHERE id = :id")
                .setParameter("embedding", embeddingKeyword)
                .setParameter("id", productEntities.getLast().getId())
                .getSingleResult();

    // Convert product entity into product dto
    List<ProductDto> productDtos = productEntities.stream().map(ProductMapper::toDto).toList();

    boolean hasMore = productDtos.size() > limit;
    String nextCursor =
        hasMore
            ? CursorCodec.encode(String.format("%s,%s", productDtos.getLast().getId(), distance))
            : null;
    if (hasMore) productDtos = productDtos.subList(0, productDtos.size() - 1);

    return new ProductsDto(productDtos, hasMore, nextCursor);
  }
}
