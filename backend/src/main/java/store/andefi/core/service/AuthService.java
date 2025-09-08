package store.andefi.core.service;

public interface AuthService {
    boolean validateCredentialsByEmailAndPassword(String email, String password);
}
