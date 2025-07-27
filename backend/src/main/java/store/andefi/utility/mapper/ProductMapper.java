package store.andefi.utility.mapper;

import store.andefi.dto.ProductDto;
import store.andefi.entity.Product;

public final class ProductMapper {
  public static ProductDto toDto(Product productEntity) {
    ProductDto productDto = new ProductDto();
    productDto.setId(productEntity.getId().toString());
    productDto.setName(productEntity.getName());
    productDto.setDescription(productEntity.getDescription());
    productDto.setPrice(productEntity.getPrice());
    productDto.setAttributes(productEntity.getAttributes());
    if (productEntity.getCategories() != null)
      productDto.setCategories(
          productEntity.getCategories().stream().map(CategoryMapper::toDto).toList());
    if (productEntity.getMedia() != null)
      productDto.setMedia(MediaMapper.toDto(productEntity.getMedia()));
    if (productEntity.getDiscount() != null)
      productDto.setDiscount(DiscountMapper.toDto(productEntity.getDiscount()));
    if (productEntity.getStock() != null)
      productDto.setStock(StockMapper.toDto(productEntity.getStock()));

    return productDto;
  }
}
