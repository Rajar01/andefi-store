package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.UUID;
import store.andefi.dto.ShippingAddressDto;
import store.andefi.dto.ShippingAddressUpdateDto;
import store.andefi.entity.Account;
import store.andefi.entity.ShippingAddress;
import store.andefi.repository.ShippingAddressRepository;
import store.andefi.utility.mapper.ShippingAddressMapper;

@ApplicationScoped
public class ShippingAddressService {
  @Inject ShippingAddressRepository shippingAddressRepository;

  public ShippingAddressDto getAccountShippingAddress(String accountId) {
    Account accountEntity =
        shippingAddressRepository
            .getEntityManager()
            .getReference(Account.class, UUID.fromString(accountId));

    return ShippingAddressMapper.toDto(accountEntity.getShippingAddress());
  }

  public void updateShippingAddress(
      String accountId, ShippingAddressUpdateDto shippingAddressUpdateDto) {
    Account accountEntity =
        shippingAddressRepository
            .getEntityManager()
            .getReference(Account.class, UUID.fromString(accountId));

    ShippingAddress shippingAddressEntity = accountEntity.getShippingAddress();
    shippingAddressEntity.setAddress(shippingAddressUpdateDto.getAddress());
    shippingAddressEntity.setAddressOtherDetails(shippingAddressUpdateDto.getAddressOtherDetails());

    shippingAddressRepository.persist(shippingAddressEntity);
  }
}
