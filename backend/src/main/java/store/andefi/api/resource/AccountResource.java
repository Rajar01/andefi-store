package store.andefi.api.resource;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.api.dto.*;
import store.andefi.core.entity.Account;
import store.andefi.core.exception.AccountVerificationException;
import store.andefi.core.exception.ResetPasswordException;
import store.andefi.core.exception.TokenException;
import store.andefi.core.security.TokenProvider;
import store.andefi.core.service.AccountService;
import store.andefi.core.service.EmailService;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Path("/api/accounts")
@RequestScoped
public class AccountResource {
    @Inject
    AccountService accountService;

    @Inject
    EmailService emailService;

    @Inject
    TokenProvider tokenProvider;

    @Inject
    @Claim(standard = Claims.sub)
    String accountId;

    @ConfigProperty(name = "app.account-verification-url")
    String accountVerificationUrl;

    @ConfigProperty(name = "app.account-verification-success-redirect-url")
    String accountVerificationSuccessRedirectUrl;

    @ConfigProperty(name = "app.reset-password-token-validation-url")
    String resetPasswordTokenValidationUrl;

    @ConfigProperty(name = "app.reset-password-token-validation-success-redirect-url")
    String resetPasswordTokenValidationSuccessRedirectUrl;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/check-if-email-available")
    public Response isEmailAvailable(IsEmailAvailableRequest request) {
        try {
            // TODO: Email validation

            if (!accountService.isEmailAvailable(request.getEmail())) {
                return Response.ok(new IsEmailAvailableResponse(request.getEmail(), false)).build();
            }

            return Response.ok(new IsEmailAvailableResponse(request.getEmail(), true)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Check if email available failed"))
                .build();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/check-if-phone-number-available")
    public Response isPhoneNumberAvailable(IsPhoneNumberAvailableRequest request) {
        try {
            // TODO: Phone number validation

            if (!accountService.isPhoneNumberAvailable(request.getPhoneNumber())) {
                return Response.ok(new IsPhoneNumberAvailableResponse(request.getPhoneNumber(), false)).build();
            }

            return Response.ok(new IsPhoneNumberAvailableResponse(request.getPhoneNumber(), true)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Check if phone number available failed"))
                .build();
        }
    }

    @GET
    @Path("/verify")
    public Response verifyAccountAndRedirect(@QueryParam("token") String token) {
        try {
            tokenProvider.validateToken(token);

            UUID accountId = tokenProvider.extractAccountIdFromToken(token);

            accountService.verifyAccount(accountId);

            return Response.seeOther(URI.create(accountVerificationSuccessRedirectUrl)).build();
        } catch (AccountVerificationException | TokenException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("ACCOUNT_VERIFICATION_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Account verification failed"))
                .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/resend-account-verification-email")
    public Response resendAccountVerificationEmail(ResendAccountVerificationEmailRequest request) {
        try {
            Optional<Account> accountOptional = accountService.findAccountByEmailOptional(request.getEmail());

            if (accountOptional.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("RESEND_ACCOUNT_VERIFICATION_EMAIL_ERROR", "Failed to resend account verification email"))
                    .build();
            }

            String token = tokenProvider.generateToken(accountOptional.get());
            String content = String.format("%s?token=%s", accountVerificationUrl, token);
            emailService.send(
                "no-reply@andefi.store",
                request.getEmail(),
                "Please Verify Your Email to Activate Your Account",
                content
            );

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to resend account verification email"))
                .build();
        }
    }

    @POST
    @Path("/reset-password-request")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendResetPasswordRequestToEmail(ForgotPasswordRequest request) {
        try {
            Optional<Account> accountOptional = accountService.findAccountByEmailOptional(request.getEmail());

            if (accountOptional.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("PASSWORD_RESET_REQUEST_ERROR", "Failed to send password reset request to email"))
                    .build();
            }

            String token = tokenProvider.generateToken(accountOptional.get());
            String content = String.format("%s?token=%s", resetPasswordTokenValidationUrl, token);
            emailService.send(
                "no-reply@andefi.store",
                request.getEmail(),
                "Reset Your Andefi Store Account Password",
                content
            );

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Failed to send password reset request to email"))
                .build();
        }
    }

    @GET
    @Path("/reset-password-token-validation")
    public Response validateResetPasswordTokenAndRedirect(@QueryParam("token") String token) {
        try {
            tokenProvider.validateToken(token);

            String redirectUrl = String.format("%s?token=%s", resetPasswordTokenValidationSuccessRedirectUrl, token);

            return Response.seeOther(URI.create(redirectUrl)).build();
        } catch (TokenException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("RESET_PASSWORD_TOKEN_VALIDATION_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Reset password token validation failed"))
                .build();
        }
    }

    @POST
    @Path("/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response resetPassword(ResetPasswordRequest request) {
        try {
            accountService.changePassword(accountId, request.getPassword());

            return Response.ok().build();
        } catch (ResetPasswordException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("RESET_PASSWORD_ERROR", e.getMessage()))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("SERVER_ERROR", "Reset password  failed"))
                .build();
        }
    }
}
