package store.andefi.infrastructure.email;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import store.andefi.core.service.EmailService;

import java.util.List;

@ApplicationScoped
public class SMTPEmailService implements EmailService {
    @Inject
    ReactiveMailer mailer;

    @Override
    public void send(String sender, String recipient, String subject, String content) {
        Mail mail = new Mail();
        mail.setFrom(sender);
        mail.setTo(List.of(recipient));
        mail.setSubject(subject);
        mail.setText(content);

        mailer.send(mail).onFailure().retry()
            .atMost(3)
            .subscribe()
            .with(success -> {}, failure -> {});
    }
}
