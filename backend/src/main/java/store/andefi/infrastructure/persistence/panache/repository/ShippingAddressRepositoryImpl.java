package store.andefi.infrastructure.persistence.panache.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.core.entity.ShippingAddress;
import store.andefi.core.repository.ShippingAddressRepository;
import store.andefi.infrastructure.persistence.panache.entity.AccountPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ShippingAddressPanacheEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ShippingAddressRepositoryImpl implements ShippingAddressRepository, PanacheRepositoryBase<ShippingAddressPanacheEntity, UUID> {
    // Convert panache entity into core entity (domain entity)
    private ShippingAddress toDomainEntity(ShippingAddressPanacheEntity shippingAddressPanacheEntity) {
        return ShippingAddress.builder()
            .id(shippingAddressPanacheEntity.getId())
            .address(shippingAddressPanacheEntity.getAddress())
            .postalCode(shippingAddressPanacheEntity.getPostalCode())
            .build();
    }

    // Use this function when created new record only
    private ShippingAddressPanacheEntity toPanacheEntity(ShippingAddress shippingAddress) {
        return ShippingAddressPanacheEntity.builder()
            .id(shippingAddress.getId())
            .address(shippingAddress.getAddress())
            .postalCode(shippingAddress.getPostalCode())
            .build();
    }

    @Override
    public Optional<ShippingAddress> findShippingAddressByAccountIdOptional(UUID accountId) {
        return find("account.id", accountId).firstResultOptional().map(this::toDomainEntity);
    }

    @Override
    public ShippingAddress save(UUID accountId, ShippingAddress shippingAddress) {
        AccountPanacheEntity accountPanacheEntity = getEntityManager()
            .getReference(AccountPanacheEntity.class, accountId);

        // Update account shipping address
        if (accountPanacheEntity.getShippingAddress() != null) {
            accountPanacheEntity.getShippingAddress().setAddress(shippingAddress.getAddress());
            accountPanacheEntity.getShippingAddress().setPostalCode(shippingAddress.getPostalCode());
            return toDomainEntity(accountPanacheEntity.getShippingAddress());
        }

        // Create account shipping address
        accountPanacheEntity.setShippingAddress(toPanacheEntity(shippingAddress));
        flush();

        return toDomainEntity(accountPanacheEntity.getShippingAddress());
    }

    @Override
    public boolean isAccountShippingAddressExist(UUID accountId) {
        AccountPanacheEntity accountPanacheEntity = getEntityManager()
            .getReference(AccountPanacheEntity.class, accountId);

        return accountPanacheEntity.getShippingAddress() != null;
    }
}
