package store.andefi.utility.mapper;

import store.andefi.dto.DiscountDto;
import store.andefi.entity.Discount;

public final class DiscountMapper {
    public static DiscountDto toDto(Discount discount) {
        return new DiscountDto(
                discount.id(),
                discount.discountPercentage(),
                discount.isActive(),
                discount.startDate(),
                discount.endDate()
        );
    }
}
