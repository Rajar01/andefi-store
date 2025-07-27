package store.andefi.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import store.andefi.dto.ProductDto;
import store.andefi.dto.ProductsDto;
import store.andefi.dto.TEIDto;
import store.andefi.entity.Product;
import store.andefi.repository.ProductRepository;
import store.andefi.utility.CursorCodec;
import store.andefi.utility.mapper.ProductMapper;

@ApplicationScoped
public class ProductService {
  @Inject ProductRepository productRepository;
  @Inject @RestClient TEIService teiService;

  public ProductsDto getProducts(String category, int limit, String cursor) {
    List<Product> productEntities;

    if (limit < 1 || limit > 100) {
      throw new BadRequestException();
    }

    if (cursor == null || cursor.isBlank()) {
      if (category.isBlank()) {
        productEntities =
            productRepository
                .findAll(Sort.by("id", Sort.Direction.Ascending))
                .page(0, limit + 1)
                .list();
      } else {
        productEntities =
            productRepository
                .find(
                    "SELECT p FROM Product p JOIN p.categories c WHERE c.name = ?1",
                    Sort.by("p.id", Sort.Direction.Ascending),
                    category)
                .page(0, limit + 1)
                .list();
      }
    } else {
      UUID c = UUID.fromString(CursorCodec.decode(cursor));

      if (category.isBlank()) {
        productEntities =
            productRepository
                .find("id >= ?1", Sort.by("id", Sort.Direction.Ascending), c)
                .page(0, limit + 1)
                .list();
      } else {
        productEntities =
            productRepository
                .find(
                    "SELECT p FROM Product p JOIN p.categories c WHERE p.id >= ?1 and c.name = ?2",
                    Sort.by("p.id", Sort.Direction.Ascending),
                    c,
                    category)
                .page(0, limit + 1)
                .list();
      }
    }

    // Convert product entity into product dto
    List<ProductDto> productDtos = productEntities.stream().map(ProductMapper::toDto).toList();

    boolean hasMore = productDtos.size() > limit;
    String nextCursor = hasMore ? CursorCodec.encode(productDtos.getLast().getId()) : null;
    if (hasMore) productDtos = productDtos.subList(0, productDtos.size() - 1);

    return new ProductsDto(productDtos, hasMore, nextCursor);
  }

  public ProductDto getProductById(String productId) {
    Product productEntity =
        productRepository
            .findByIdOptional(UUID.fromString(productId))
            .orElseThrow(NotFoundException::new);

    return ProductMapper.toDto(productEntity);
  }

  public ProductsDto search(String keyword, int limit, String cursor) {
    if (keyword.isBlank()) throw new BadRequestException();

    float[][] embeddingKeyword = teiService.getEmbedding(new TEIDto(keyword));

    return productRepository.semanticSearch(embeddingKeyword[0], limit, cursor);
  }
}
