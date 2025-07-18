package store.andefi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.mindrot.jbcrypt.BCrypt;
import store.andefi.dto.*;
import store.andefi.entity.Account;
import store.andefi.repository.AccountRepository;
import store.andefi.repository.RoleRepository;
import store.andefi.utility.mapper.AccountMapper;
import store.andefi.utility.mapper.RoleMapper;

@ApplicationScoped
public class AuthenticationService {
  @ConfigProperty(name = "api.gateway.base-url")
  String apiGatewayBaseUrl;

  @ConfigProperty(name = "password-reset.url")
  String passwordResetUrl;

  @Inject AccountRepository accountRepository;
  @Inject RoleRepository roleRepository;

  @Inject AccountService accountService;
  @Inject JwtTokenService jwtTokenService;

  @Channel("account-service")
  Emitter<String> emitter;

  @Inject JWTParser jwtParser;

  @Inject ObjectMapper objectMapper;

  public String signIn(AccountLoginDto accountLoginDto) {
    Account accountEntity =
        accountRepository
            .find("email", accountLoginDto.email())
            .firstResultOptional()
            .orElseThrow(NotFoundException::new);

    if (!BCrypt.checkpw(accountLoginDto.password(), accountEntity.password())) {
      throw new AuthenticationFailedException();
    }

    List<RoleDto> roleDtos =
        accountEntity.roleIds().stream()
            .map(roleId -> roleRepository.findByIdOptional(new ObjectId(roleId)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(RoleMapper::toDto)
            .toList();

    AccountDto accountDto = AccountMapper.toDto(accountEntity);
    accountDto.setRoles(roleDtos);

    return jwtTokenService.generateToken(accountDto);
  }

  public void signOut() {}

  public void verifyAccount(String token) throws ParseException {
    // Parse and verify token
    JsonWebToken jwt = jwtParser.parse(token);

    AccountDto accountDto = accountService.getAccountById(jwt.getSubject());

    AccountUpdateDto accountUpdateDto =
        new AccountUpdateDto(
            accountDto.getId().transform(ObjectId::new),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of(true),
            Optional.of(Instant.now()));

    accountService.updateAccount(accountUpdateDto);
  }

  public String validatePasswordResetTokenAndGenerateRedirectUrl(String token)
      throws ParseException {
    // Parse and verify token
    JsonWebToken jwt = jwtParser.parse(token);

    return String.format("%s?token=%s", passwordResetUrl, token);
  }

  public void resetPassword(PasswordResetDto passwordResetDto) throws ParseException {
    // Parse and verify token
    JsonWebToken jwt = jwtParser.parse(passwordResetDto.token());

    AccountDto accountDto = accountService.getAccountById(jwt.getSubject());

    AccountUpdateDto accountUpdateDto =
        new AccountUpdateDto(
            accountDto.getId().transform(ObjectId::new),
            Optional.empty(),
            Optional.empty(),
            Optional.of(passwordResetDto.password()),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    accountService.updateAccount(accountUpdateDto);
  }

  public void sendAccountVerificationEmail(AccountDto accountDto) throws JsonProcessingException {
    String token = jwtTokenService.generateToken(accountDto);
    String accountVerificationLink = apiGatewayBaseUrl + "/api/accounts/verify?token=" + token;

    EmailMessageDto emailMessageDto =
        new EmailMessageDto(
            "no-reply@andefi.store",
            List.of(accountDto.getEmail()),
            "Please Verify Your Email to Activate Your Account",
            accountVerificationLink);

    String emailMessageJson = objectMapper.writeValueAsString(emailMessageDto);

    emitter.send(emailMessageJson);
  }

  public void resendAccountVerificationEmail(String accountEmail) throws JsonProcessingException {
    AccountDto accountDto = accountService.getAccountByEmail(accountEmail);
    sendAccountVerificationEmail(accountDto);
  }

  public void sendPasswordResetEmail(String accountEmail) throws JsonProcessingException {
    AccountDto accountDto = accountService.getAccountByEmail(accountEmail);
    String token = jwtTokenService.generateToken(accountDto);
    String accountPasswordResetLink =
        apiGatewayBaseUrl + "/api/accounts/reset-password?token=" + token;

    EmailMessageDto emailMessageDto =
        new EmailMessageDto(
            "no-reply@andefi.store",
            List.of(accountDto.getEmail()),
            "Reset Your Andefi Store Account Password",
            accountPasswordResetLink);

    String emailMessageJson = objectMapper.writeValueAsString(emailMessageDto);

    emitter.send(emailMessageJson);
  }
}
