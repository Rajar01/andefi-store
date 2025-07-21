package store.andefi.utility.mapper;

import store.andefi.dto.ShippingAddressDto;
import store.andefi.entity.ShippingAddress;

public final class ShippingAddressMapper {
  public static ShippingAddressDto toDto(ShippingAddress shippingAddress) {
    return new ShippingAddressDto(
        shippingAddress.id().toString(), shippingAddress.accountId(), shippingAddress.address());
  }
}
