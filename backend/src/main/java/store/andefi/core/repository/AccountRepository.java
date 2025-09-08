package store.andefi.core.repository;

import store.andefi.core.entity.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findAccountByIdOptional(UUID id);

    Optional<Account> findAccountByEmailOptional(String email);

    boolean isEmailExist(String email);

    boolean isPhoneNumberExist(String phoneNumber);

    Account save(Account account);
}
