package store.andefi.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import store.andefi.dto.DiscountDto;
import store.andefi.dto.MediaDto;
import store.andefi.dto.ProductCatalogDto;
import store.andefi.dto.ProductDto;
import store.andefi.entity.Product;
import store.andefi.repository.ProductRepository;
import store.andefi.utility.CursorCodec;
import store.andefi.utility.mapper.ProductMapper;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    @Inject
    @RestClient
    MediaService mediaService;

    @Inject
    @RestClient
    DiscountService discountService;

    public ProductCatalogDto getProductCatalog(int limit, String cursor) {
        List<Product> productEntities;

        if (limit < 1 || limit > 100) {
            throw new BadRequestException();
        }

        if (cursor == null || cursor.isBlank()) {
            productEntities = productRepository.findAll(Sort.by("_id", Sort.Direction.Ascending)).page(0, limit + 1).list();
        } else {
            UUID c = UUID.fromString(CursorCodec.decode(cursor));

            productEntities = productRepository.find("_id >= ?1", Sort.by("_id", Sort.Direction.Ascending), c).page(0, limit + 1).list();
        }

        // Convert product entity into product dto
        List<ProductDto> productDtos = productEntities.stream().map(productEntity -> {
            MediaDto media = mediaService.getMediaById(productEntity.mediaId());
            DiscountDto discount = discountService.getDiscountById(productEntity.discountId());

            ProductDto productDto = ProductMapper.toDto(productEntity);
            productDto.setMedia(media);
            productDto.setDiscount(discount);
            return productDto;
        }).toList();

        boolean hasMore = productDtos.size() > limit;
        String nextCursor = hasMore ? CursorCodec.encode(productDtos.getLast().getId()) : null;
        if (hasMore) productDtos = productDtos.subList(0, productDtos.size() - 1);

        return new ProductCatalogDto(productDtos, hasMore, nextCursor);
    }

    public ProductDto getProductById(String productId) {
        return productRepository.findByIdOptional(UUID.fromString(productId)).map(productEntity -> {
            MediaDto media = mediaService.getMediaById(productEntity.mediaId());
            DiscountDto discount = discountService.getDiscountById(productEntity.discountId());

            ProductDto productDto = ProductMapper.toDto(productEntity);
            productDto.setMedia(media);
            productDto.setDiscount(discount);
            return productDto;
        }).orElseThrow(NotFoundException::new);
    }
}
