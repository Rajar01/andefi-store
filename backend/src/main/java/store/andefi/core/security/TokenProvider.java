package store.andefi.core.security;

import store.andefi.core.entity.Account;
import store.andefi.core.exception.TokenException;

import java.util.UUID;

public interface TokenProvider {
    String generateToken(Account account);

    void validateToken(String token) throws TokenException;

    UUID extractAccountIdFromToken(String token) throws TokenException;
}
