package store.andefi.core.repository;

import store.andefi.core.entity.ShippingAddress;

import java.util.Optional;
import java.util.UUID;

public interface ShippingAddressRepository {
    Optional<ShippingAddress> findShippingAddressByAccountIdOptional(UUID accountId);

    ShippingAddress save(UUID accountId, ShippingAddress shippingAddress);

    boolean isAccountShippingAddressExist(UUID accountId);
}
