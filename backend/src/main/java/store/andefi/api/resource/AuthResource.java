package store.andefi.api.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import store.andefi.api.dto.AccountSignInRequest;
import store.andefi.api.dto.AccountSignInResponse;
import store.andefi.api.dto.AccountSignUpRequest;
import store.andefi.api.dto.ErrorResponse;
import store.andefi.core.entity.Account;
import store.andefi.core.enums.Role;
import store.andefi.core.exception.AccountCreationException;
import store.andefi.core.security.TokenProvider;
import store.andefi.core.service.AccountService;
import store.andefi.core.service.AuthService;
import store.andefi.core.service.CartService;
import store.andefi.core.service.EmailService;

import java.util.Optional;

@Path("/api/auth")
public class AuthResource {
    @Inject
    AccountService accountService;

    @Inject
    AuthService authService;

    @Inject
    EmailService emailService;

    @Inject
    TokenProvider tokenProvider;

    @ConfigProperty(name = "app.account-verification-url")
    String accountVerificationUrl;

    @POST
    @Path("/sign-in")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(@Valid AccountSignInRequest request) {
        try {
            if (!authService.validateCredentialsByEmailAndPassword(request.getEmail(), request.getPassword())) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("UNAUTHORIZED", "Invalid credentials"))
                    .build();
            }

            Optional<Account> accountOptional = accountService.findAccountByEmailOptional(request.getEmail());
            if (accountOptional.isEmpty()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("UNAUTHORIZED", "Invalid credentials"))
                    .build();
            }

            String token = tokenProvider.generateToken(accountOptional.get());
            return Response.ok(new AccountSignInResponse(token, "Bearer")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Sign in failed"))
                .build();
        }
    }

    @POST
    @Path("/sign-up")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(@Valid AccountSignUpRequest request) {
        try {
            Account newAccount = Account.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.CUSTOMER)
                .shippingAddress(null)
                .build();

            Account createdAccount = accountService.createAccount(newAccount);

            // Send account verification email
            String token = tokenProvider.generateToken(createdAccount);
            String content = String.format("%s?token=%s", accountVerificationUrl, token);
            emailService.send(
                "no-reply@andefi.store",
                request.getEmail(),
                "Please Verify Your Email to Activate Your Account",
                content
            );

            return Response.status(Response.Status.CREATED).build();
        } catch (AccountCreationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("SIGN_UP_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Sign up failed"))
                .build();
        }
    }
}
