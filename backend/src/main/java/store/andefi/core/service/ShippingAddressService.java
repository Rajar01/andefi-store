package store.andefi.core.service;

import store.andefi.core.entity.ShippingAddress;
import store.andefi.core.exception.ShippingAddressCreationException;
import store.andefi.core.exception.ShippingAddressException;

import java.util.Optional;
import java.util.UUID;

public interface ShippingAddressService {
    Optional<ShippingAddress> getShippingAddressByAccountIdOptional(UUID accountId);

    ShippingAddress createAccountShippingAddress(UUID accountId, ShippingAddress shippingAddress) throws ShippingAddressCreationException;

    ShippingAddress updateAccountShippingAddress(UUID accountId, ShippingAddress shippingAddress) throws ShippingAddressException;

    boolean isAccountShippingAddressExist(UUID accountId);
}
