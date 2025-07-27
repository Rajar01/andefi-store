package store.andefi.utility.mapper;

import store.andefi.dto.ShippingAddressDto;
import store.andefi.entity.ShippingAddress;

public final class ShippingAddressMapper {
  public static ShippingAddressDto toDto(ShippingAddress shippingAddressEntity) {
    ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
    shippingAddressDto.setId(shippingAddressEntity.getId().toString());
    shippingAddressDto.setAddress(shippingAddressEntity.getAddress());
    shippingAddressDto.setAddressOtherDetails(shippingAddressEntity.getAddressOtherDetails());

    return shippingAddressDto;
  }
}
