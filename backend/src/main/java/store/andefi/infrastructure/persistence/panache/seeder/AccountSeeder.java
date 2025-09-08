package store.andefi.infrastructure.persistence.panache.seeder;

import com.github.javafaker.Faker;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import store.andefi.core.enums.Role;
import store.andefi.infrastructure.persistence.panache.entity.AccountPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ShippingAddressPanacheEntity;
import store.andefi.infrastructure.persistence.panache.repository.AccountRepositoryImpl;
import store.andefi.infrastructure.security.BCryptPasswordHasher;

import java.util.Locale;

@ApplicationScoped
@Priority(1)
public class AccountSeeder implements Seeder {
    @Inject
    AccountRepositoryImpl accountRepository;

    @Inject
    BCryptPasswordHasher passwordHasher;

    @Override
    public void run() {
        // If table already have data then just return
        boolean isEmpty = accountRepository.getEntityManager()
            .createQuery("FROM AccountPanacheEntity p ORDER BY p.id ASC", AccountPanacheEntity.class)
            .setMaxResults(1)
            .getResultList()
            .isEmpty();

        if (!isEmpty) return;

        Faker faker = new Faker(Locale.forLanguageTag("in-ID"));

        AccountPanacheEntity customerOneAccountPanacheEntity = AccountPanacheEntity.builder()
            .email("customer1@example.com")
            .password(passwordHasher.hash("12345678"))
            .fullName(faker.name().fullName())
            .phoneNumber("+6281234567891")
            .role(Role.CUSTOMER)
            .shippingAddress(ShippingAddressPanacheEntity.builder().address(faker.address().fullAddress()).postalCode(faker.address().zipCode()).build())
            .build();

        accountRepository.getEntityManager().persist(customerOneAccountPanacheEntity);

        AccountPanacheEntity customerTwoAccountPanacheEntity = AccountPanacheEntity.builder()
            .email("customer2@example.com")
            .password(passwordHasher.hash("12345678"))
            .fullName(faker.name().fullName())
            .phoneNumber("+6281234567892")
            .role(Role.CUSTOMER)
            .build();

        accountRepository.getEntityManager().persist(customerTwoAccountPanacheEntity);
    }
}
