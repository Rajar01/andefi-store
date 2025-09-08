package store.andefi.core.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import store.andefi.core.entity.ShippingAddress;
import store.andefi.core.exception.ShippingAddressCreationException;
import store.andefi.core.exception.ShippingAddressException;
import store.andefi.core.repository.ShippingAddressRepository;
import store.andefi.core.service.ShippingAddressService;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ShippingAddressServiceImpl implements ShippingAddressService {
    @Inject
    ShippingAddressRepository shippingAddressRepository;

    @Override
    public Optional<ShippingAddress> getShippingAddressByAccountIdOptional(UUID accountId) {
        return shippingAddressRepository.findShippingAddressByAccountIdOptional(accountId);
    }

    @Override
    @Transactional
    public ShippingAddress createAccountShippingAddress(UUID accountId, ShippingAddress shippingAddress) throws ShippingAddressCreationException {
        if (shippingAddress.getAddress() == null || shippingAddress.getAddress().isBlank()) {
            throw new ShippingAddressCreationException("Address can not be null or blank");
        }

        if (shippingAddress.getPostalCode() == null || shippingAddress.getPostalCode().isBlank()) {
            throw new ShippingAddressCreationException("Postal code can not be null or blank");
        }

        return shippingAddressRepository.save(accountId, shippingAddress);
    }

    @Override
    @Transactional
    public ShippingAddress updateAccountShippingAddress(UUID accountId, ShippingAddress shippingAddress) throws ShippingAddressException {
        if (shippingAddress.getAddress() == null || shippingAddress.getAddress().isBlank()) {
            throw new ShippingAddressCreationException("Address can not be null or blank");
        }

        if (shippingAddress.getPostalCode() == null || shippingAddress.getPostalCode().isBlank()) {
            throw new ShippingAddressCreationException("Postal code can not be null or blank");
        }

        return shippingAddressRepository.save(accountId, shippingAddress);
    }

    @Override
    public boolean isAccountShippingAddressExist(UUID accountId) {
        return shippingAddressRepository.isAccountShippingAddressExist(accountId);
    }
}
