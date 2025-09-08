package store.andefi.core.service;

public interface EmailService {
    void send(String sender, String recipient, String subject, String content);
}
