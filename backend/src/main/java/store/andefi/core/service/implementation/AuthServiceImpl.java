package store.andefi.core.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import store.andefi.core.entity.Account;
import store.andefi.core.repository.AccountRepository;
import store.andefi.core.security.PasswordHasher;
import store.andefi.core.service.AuthService;

import java.util.Optional;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {
    @Inject
    AccountRepository accountRepository;

    @Inject
    PasswordHasher passwordHasher;

    @Override
    public boolean validateCredentialsByEmailAndPassword(String email, String password) {
        // Check if account exist by email
        Optional<Account> accountOptional = accountRepository.findAccountByEmailOptional(email);
        if (accountOptional.isEmpty()) {
            return false;
        }

        // Check if plain password matches hashed password
        String hashedPassword = accountOptional.get().getPassword();
        return passwordHasher.matches(password, hashedPassword);
    }
}
