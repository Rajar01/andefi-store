package store.andefi.utility.mapper;

import store.andefi.dto.DiscountDto;
import store.andefi.entity.Discount;

public final class DiscountMapper {
  public static DiscountDto toDto(Discount discountEntity) {
    DiscountDto discountDto = new DiscountDto();
    discountDto.setId(discountEntity.getId().toString());
    discountDto.setDiscountPercentage(discountEntity.getDiscountPercentage());
    discountDto.setActive(discountEntity.isActive());
    discountDto.setStartDate(discountEntity.getStartDate());
    discountDto.setEndDate(discountEntity.getEndDate());

    return discountDto;
  }
}
