package store.andefi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import store.andefi.dto.EmailMessageDto;

@ApplicationScoped
public class EmailService {
    @Inject
    ReactiveMailer mailer;

    @Inject
    ObjectMapper objectMapper;

    @Incoming("email-service")
    public void send(String emailMessageJson) throws JsonProcessingException {
        EmailMessageDto emailMessageDto = objectMapper.readValue(emailMessageJson, EmailMessageDto.class);

        Mail mail = new Mail();
        mail.setFrom(emailMessageDto.from());
        mail.setTo(emailMessageDto.to());
        mail.setSubject(emailMessageDto.subject());
        mail.setText(emailMessageDto.body());

        mailer.send(mail).onFailure().retry().atMost(3)
                .subscribe()
                .with(success -> {},failure -> {});
    }
}
