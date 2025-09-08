package store.andefi.core.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import store.andefi.core.entity.Account;
import store.andefi.core.exception.AccountCreationException;
import store.andefi.core.exception.AccountVerificationException;
import store.andefi.core.exception.ResetPasswordException;
import store.andefi.core.repository.AccountRepository;
import store.andefi.core.security.PasswordHasher;
import store.andefi.core.service.AccountService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+62|0)8[1-9][0-9]{7,13}$");

    @Inject
    AccountRepository accountRepository;

    @Inject
    PasswordHasher passwordHasher;

    @Override
    public Optional<Account> findAccountByIdOptional(UUID id) {
        return accountRepository.findAccountByIdOptional(id);
    }

    @Override
    public Optional<Account> findAccountByEmailOptional(String email) {
        return accountRepository.findAccountByEmailOptional(email);
    }

    @Override
    @Transactional
    public Account createAccount(Account account) throws AccountCreationException {
        // Email validation
        if (account.getEmail() == null || !EMAIL_PATTERN.matcher(account.getEmail()).matches()) {
            throw new AccountCreationException("Invalid email format");
        }

        // Password validation
        if (account.getPassword() == null || account.getPassword().length() < 8) {
            throw new AccountCreationException("Password must be at least 8 characters long");
        }

        if (account.getPassword().length() > 100) {
            throw new AccountCreationException("Password is too long");
        }

        // Full name validation
        if (account.getFullName() == null) {
            throw new AccountCreationException("Full name cannot blank");
        }

        // Phone number validation
        if (account.getPhoneNumber() == null || !PHONE_PATTERN.matcher(account.getPhoneNumber()).matches()) {
            throw new AccountCreationException("Invalid phone number format");
        }

        if (accountRepository.isEmailExist(account.getEmail())) {
            throw new AccountCreationException("Email is already registered");
        }

        if (accountRepository.isPhoneNumberExist(account.getPhoneNumber())) {
            throw new AccountCreationException("Phone number is already registered");
        }

        String hashedPassword = passwordHasher.hash(account.getPassword());
        account.setPassword(hashedPassword);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void verifyAccount(UUID accountId) throws AccountVerificationException {
        Optional<Account> accountOptional = findAccountByIdOptional(accountId);

        if (accountOptional.isEmpty()) {
            throw new AccountVerificationException("Account verification failed");
        }

        accountOptional.get().setVerifiedAt(Instant.now());

        accountRepository.save(accountOptional.get());
    }

    @Override
    @Transactional
    public void changePassword(String accountId, String password) {
        // Password validation
        if (password == null || password.length() < 8) {
            throw new AccountCreationException("Password must be at least 8 characters long");
        }

        if (password.length() > 100) {
            throw new AccountCreationException("Password is too long");
        }

        Optional<Account> accountOptional = findAccountByIdOptional(UUID.fromString(accountId));

        if (accountOptional.isEmpty()) {
            throw new ResetPasswordException("Reset password failed");
        }

        Account account = accountOptional.get();
        String hashedPassword = passwordHasher.hash(password);
        account.setPassword(hashedPassword);

        accountRepository.save(account);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !accountRepository.isEmailExist(email);
    }

    @Override
    public boolean isPhoneNumberAvailable(String phoneNumber) {
        return !accountRepository.isPhoneNumberExist(phoneNumber);
    }
}
