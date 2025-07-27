package store.andefi.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mindrot.jbcrypt.BCrypt;
import store.andefi.dto.AccountDto;
import store.andefi.dto.AccountSignInDto;
import store.andefi.dto.AccountSignUpDto;
import store.andefi.entity.Account;
import store.andefi.entity.Role;
import store.andefi.entity.ShippingAddress;
import store.andefi.exception.DuplicationException;
import store.andefi.repository.AccountRepository;
import store.andefi.repository.RoleRepository;
import store.andefi.utility.mapper.AccountMapper;

@ApplicationScoped
public class AccountService {
  @Inject AccountRepository accountRepository;
  @Inject RoleRepository roleRepository;

  @Inject JwtTokenService jwtTokenService;

  @Inject JWTParser jwtParser;
  @Inject ReactiveMailer mailer;

  @ConfigProperty(name = "app.base-url")
  String appBaseUrl;

  @ConfigProperty(name = "account-verification-success-redirect-url")
  String accountVerificationSuccessRedirectUrl;

  @ConfigProperty(name = "password-reset-redirect-url")
  String passwordResetRedirectUrl;

  public String signIn(AccountSignInDto accountSignInDto) {
    Account accountEntity =
        accountRepository
            .find("email", accountSignInDto.getEmail())
            .firstResultOptional()
            .orElseThrow(AuthenticationFailedException::new);

    if (!BCrypt.checkpw(accountSignInDto.getPassword(), accountEntity.getPassword())) {
      throw new AuthenticationFailedException();
    }

    AccountDto accountDto = AccountMapper.toDto(accountEntity);

    return jwtTokenService.generateToken(accountDto);
  }

  public void signUp(AccountSignUpDto accountSignUpDto) {
    Predicate<String> isEmailAlreadyExist =
        email -> accountRepository.find("email", email).firstResultOptional().isPresent();

    Predicate<String> isUsernameAlreadyExist =
        username -> accountRepository.find("username", username).firstResultOptional().isPresent();

    // Check if email or username name already exist
    if (isEmailAlreadyExist.test(accountSignUpDto.getEmail())
        || isUsernameAlreadyExist.test(accountSignUpDto.getUsername())) {
      throw new DuplicationException();
    }

    // Get role entities
    Set<Role> roleEntities =
        accountSignUpDto.getRoles().stream()
            .map(
                role ->
                    roleRepository
                        .find("name", role)
                        .firstResultOptional()
                        .orElseThrow(RuntimeException::new))
            .collect(Collectors.toSet());

    Account accountEntity = new Account();
    accountEntity.setEmail(accountSignUpDto.getEmail());
    accountEntity.setUsername(accountSignUpDto.getUsername());
    accountEntity.setFullName(accountSignUpDto.getFullName());
    accountEntity.setPhoneNumber(accountSignUpDto.getPhoneNumber());
    accountEntity.setShippingAddress(new ShippingAddress());
    accountEntity.setPassword(BCrypt.hashpw(accountSignUpDto.getPassword(), BCrypt.gensalt()));
    accountEntity.setRoles(roleEntities);
    accountEntity.setVerified(false);

    accountRepository.persist(accountEntity);

    AccountDto accountDto = AccountMapper.toDto(accountEntity);
    sendAccountVerificationEmail(accountDto);
  }

  public String verifyAccountAndGenerateRedirectUrl(String token) throws ParseException {
    // Parse and verify token
    JsonWebToken jwt = jwtParser.parse(token);

    Account accountEntity =
        accountRepository
            .findByIdOptional(UUID.fromString(jwt.getSubject()))
            .orElseThrow(RuntimeException::new);

    accountEntity.setVerified(true);
    accountEntity.setVerifiedAt(Instant.now());

    accountRepository.persist(accountEntity);

    return accountVerificationSuccessRedirectUrl;
  }

  public String validatePasswordResetTokenAndGenerateRedirectUrl(String token)
      throws ParseException {
    // Parse and verify token
    JsonWebToken jwt = jwtParser.parse(token);

    return String.format("%s?token=%s", passwordResetRedirectUrl, token);
  }

  public void resetPassword(String accountId, String password) {
    Account accountEntity =
        accountRepository
            .findByIdOptional(UUID.fromString(accountId))
            .orElseThrow(RuntimeException::new);

    accountEntity.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

    accountRepository.persist(accountEntity);
  }

  public void sendAccountVerificationEmail(AccountDto accountDto) {
    String token = jwtTokenService.generateToken(accountDto);
    String accountVerificationLink = appBaseUrl + "/api/accounts/verify?token=" + token;

    Mail mail = new Mail();
    mail.setFrom("no-reply@andefi.store");
    mail.setTo(List.of(accountDto.getEmail()));
    mail.setSubject("Please Verify Your Email to Activate Your Account");
    mail.setText(accountVerificationLink);

    mailer.send(mail).onFailure().retry().atMost(3).subscribe().with(success -> {}, failure -> {});
  }

  public void resendAccountVerificationEmail(String accountEmail) {
    Account accountEntity =
        accountRepository
            .find("email", accountEmail)
            .firstResultOptional()
            .orElseThrow(RuntimeException::new);

    AccountDto accountDto = AccountMapper.toDto(accountEntity);
    sendAccountVerificationEmail(accountDto);
  }

  public void sendPasswordResetEmail(String accountEmail) {
    Account accountEntity =
        accountRepository
            .find("email", accountEmail)
            .firstResultOptional()
            .orElseThrow(RuntimeException::new);
    AccountDto accountDto = AccountMapper.toDto(accountEntity);

    String token = jwtTokenService.generateToken(accountDto);
    String accountPasswordResetLink = appBaseUrl + "/api/accounts/reset-password?token=" + token;

    Mail mail = new Mail();
    mail.setFrom("no-reply@andefi.store");
    mail.setTo(List.of(accountDto.getEmail()));
    mail.setSubject("Reset Your Andefi Store Account Password");
    mail.setText(accountPasswordResetLink);

    mailer.send(mail).onFailure().retry().atMost(3).subscribe().with(success -> {}, failure -> {});
  }
}
