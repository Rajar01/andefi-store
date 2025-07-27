package store.andefi.resource;

import io.quarkus.security.Authenticated;
import io.smallrye.jwt.auth.principal.ParseException;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import store.andefi.dto.AccountSignInDto;
import store.andefi.dto.AccountSignUpDto;
import store.andefi.service.AccountService;

@Path("/api/accounts")
@RequestScoped
public class AccountResource {
  @Inject AccountService accountService;

  @Inject
  @Claim(standard = Claims.sub)
  String accountId;

  @POST
  @Path("/sign-in")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response signIn(AccountSignInDto accountSignInDto) {
    String token = accountService.signIn(accountSignInDto);

    return Response.ok(token).build();
  }

  @POST
  @Path("/sign-up")
  @Transactional
  @Consumes(MediaType.APPLICATION_JSON)
  public Response signUp(AccountSignUpDto accountSignUpDto) {
    accountService.signUp(accountSignUpDto);

    return Response.status(Response.Status.CREATED).build();
  }

  @GET
  @Transactional
  @Path("/verify")
  public Response verifyAccount(@QueryParam("token") String token) throws ParseException {
    String redirectUrl = accountService.verifyAccountAndGenerateRedirectUrl(token);

    return Response.seeOther(URI.create(redirectUrl)).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/resend-verification-email")
  public Response resendAccountVerificationEmail(JsonObject payload) {
    accountService.resendAccountVerificationEmail(payload.getString("email"));

    return Response.ok().build();
  }

  @POST
  @Path("/forgot-password")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response forgotPassword(JsonObject payload) {
    accountService.sendPasswordResetEmail(payload.getString("email"));

    return Response.ok().build();
  }

  @GET
  @Path("/reset-password")
  public Response validatePasswordResetTokenAndRedirect(@QueryParam("token") String token)
      throws ParseException {
    String redirectUrl = accountService.validatePasswordResetTokenAndGenerateRedirectUrl(token);

    return Response.seeOther(URI.create(redirectUrl)).build();
  }

  @POST
  @Path("/reset-password")
  @Consumes(MediaType.APPLICATION_JSON)
  @Authenticated
  @Transactional
  public Response resetPassword(JsonObject payload) throws ParseException {
    accountService.resetPassword(accountId, payload.getString("password"));

    return Response.ok().build();
  }
}
