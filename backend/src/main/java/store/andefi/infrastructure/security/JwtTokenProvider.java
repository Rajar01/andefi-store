package store.andefi.infrastructure.security;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import store.andefi.core.entity.Account;
import store.andefi.core.exception.TokenException;
import store.andefi.core.security.TokenProvider;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class JwtTokenProvider implements TokenProvider {
    @Inject
    JWTParser jwtParser;

    @ConfigProperty(name = "jwt.issuer")
    String jwtIssuer;

    @Override
    public String generateToken(Account account) {
        Instant now = Instant.now();

        return Jwt.issuer(jwtIssuer)
            .subject(account.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plus(Duration.ofHours(1)))
            .claim(Claims.full_name.name(), account.getFullName())
            .claim(Claims.email.name(), account.getEmail())
            .claim(Claims.phone_number.name(), account.getPhoneNumber())
            .claim(Claims.email_verified.name(), account.getVerifiedAt() != null)
            .sign();
    }

    @Override
    public void validateToken(String token) throws TokenException {
        try {
            jwtParser.parse(token);
        } catch (ParseException e) {
            throw new TokenException("Invalid token");
        }
    }

    @Override
    public UUID extractAccountIdFromToken(String token) throws TokenException {
        try {
            JsonWebToken jsonWebToken = jwtParser.parse(token);
            return UUID.fromString(jsonWebToken.getSubject());
        } catch (ParseException e) {
            throw new TokenException("Failed to extract account id from token");
        }
    }
}
