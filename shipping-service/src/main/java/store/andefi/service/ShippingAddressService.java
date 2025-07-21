package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import store.andefi.dto.ShippingAddressDto;
import store.andefi.repository.ShippingAddressRepository;
import store.andefi.utility.mapper.ShippingAddressMapper;

@ApplicationScoped
public class ShippingAddressService {
  @Inject ShippingAddressRepository shippingAddressRepository;

  public ShippingAddressDto getAccountShippingAddress(String accountId) {
    return shippingAddressRepository
        .find("account_id", accountId)
        .firstResultOptional()
        .map(ShippingAddressMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }
}
