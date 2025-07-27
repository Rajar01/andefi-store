package store.andefi.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.time.Instant;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.dto.AccountDto;

@ApplicationScoped
public class JwtTokenService {
  @ConfigProperty(name = "jwt.issuer")
  String jwtIssuer;

  public String generateToken(AccountDto accountDto) {
    Instant now = Instant.now();

    return Jwt.issuer(jwtIssuer)
        .subject(accountDto.getId())
        .issuedAt(now)
        .expiresAt(now.plus(Duration.ofMinutes(5)))
        .groups(accountDto.getRoles())
        .claim(Claims.email.name(), accountDto.getEmail())
        .claim(Claims.preferred_username.name(), accountDto.getUsername())
        .sign();
  }
}
