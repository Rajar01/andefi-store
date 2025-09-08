package store.andefi.core.service;

import store.andefi.core.entity.Account;
import store.andefi.core.exception.AccountCreationException;
import store.andefi.core.exception.AccountVerificationException;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    Optional<Account> findAccountByIdOptional(UUID id);

    Optional<Account> findAccountByEmailOptional(String email);

    Account createAccount(Account account) throws AccountCreationException;

    void verifyAccount(UUID accountId) throws AccountVerificationException;

    void changePassword(String accountId, String password);

    boolean isEmailAvailable(String email);

    boolean isPhoneNumberAvailable(String phoneNumber);
}
