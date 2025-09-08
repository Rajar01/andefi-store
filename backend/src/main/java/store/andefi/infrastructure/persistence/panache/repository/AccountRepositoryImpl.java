package store.andefi.infrastructure.persistence.panache.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.core.entity.Account;
import store.andefi.core.entity.ShippingAddress;
import store.andefi.core.repository.AccountRepository;
import store.andefi.infrastructure.persistence.panache.entity.AccountPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ShippingAddressPanacheEntity;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AccountRepositoryImpl implements AccountRepository, PanacheRepositoryBase<AccountPanacheEntity, UUID> {
    // Convert panache entity into core entity (domain entity)
    private Account toDomainEntity(AccountPanacheEntity accountPanacheEntity) {
        ShippingAddress shippingAddress = Optional.ofNullable(accountPanacheEntity.getShippingAddress())
            .map(it -> ShippingAddress.builder()
                .id(it.getId())
                .address(it.getAddress())
                .postalCode(it.getPostalCode())
                .build())
            .orElse(null);

        return Account.builder()
            .id(accountPanacheEntity.getId())
            .fullName(accountPanacheEntity.getFullName())
            .email(accountPanacheEntity.getEmail())
            .phoneNumber(accountPanacheEntity.getPhoneNumber())
            .password(accountPanacheEntity.getPassword())
            .verifiedAt(accountPanacheEntity.getVerifiedAt())
            .role(accountPanacheEntity.getRole())
            .shippingAddress(shippingAddress)
            .build();
    }

    // Use this function when created new record only
    private AccountPanacheEntity toPanacheEntity(Account account) {
        ShippingAddressPanacheEntity shippingAddressPanacheEntity = Optional.ofNullable(account.getShippingAddress())
            .map(it -> ShippingAddressPanacheEntity.builder()
                .id(account.getShippingAddress().getId())
                .address(account.getShippingAddress().getAddress())
                .postalCode(account.getShippingAddress().getPostalCode())
                .build())
            .orElse(null);

        return AccountPanacheEntity.builder()
            .id(account.getId())
            .email(account.getEmail())
            .password(account.getPassword())
            .fullName(account.getFullName())
            .phoneNumber(account.getPhoneNumber())
            .verifiedAt(account.getVerifiedAt())
            .role(account.getRole())
            .shippingAddress(shippingAddressPanacheEntity)
            .build();
    }

    @Override
    public Optional<Account> findAccountByIdOptional(UUID id) {
        return findByIdOptional(id).map(this::toDomainEntity);
    }

    @Override
    public Optional<Account> findAccountByEmailOptional(String email) {
        return find("email", email).firstResultOptional().map(this::toDomainEntity);
    }

    @Override
    public boolean isEmailExist(String email) {
        return count("email", email) > 0;
    }

    @Override
    public boolean isPhoneNumberExist(String phoneNumber) {
        return count("phoneNumber", phoneNumber) > 0;
    }

    @Override
    public Account save(Account account) {
        // Update account information
        if (account.getId() != null) {
            Optional<AccountPanacheEntity> existingAccountPanacheEntityOptional = findByIdOptional(account.getId());

            // TODO: Reset account verified at to null if email is change
            // TODO: Check if email and phone number is not exist before update email and phone number
            existingAccountPanacheEntityOptional.ifPresent(it -> {
                it.setEmail(account.getEmail());
                it.setPassword(account.getPassword());
                it.setFullName(account.getFullName());
                it.setPhoneNumber(account.getPhoneNumber());
                it.setVerifiedAt(account.getVerifiedAt());

                if (account.getShippingAddress() != null) {
                    it.getShippingAddress().setAddress(account.getShippingAddress().getAddress());
                    it.getShippingAddress().setPostalCode(account.getShippingAddress().getPostalCode());
                }

                persist(it);
            });

            return toDomainEntity(existingAccountPanacheEntityOptional.get());
        }

        // Create new account
        persist(toPanacheEntity(account));
        Optional<Account> newAccountOptional = findAccountByEmailOptional(account.getEmail());
        return newAccountOptional.get();
    }
}
