package store.andefi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.smallrye.jwt.auth.principal.ParseException;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import store.andefi.dto.AccountLoginDto;
import store.andefi.dto.PasswordResetDto;
import store.andefi.service.AuthenticationService;

@Path("/api/accounts")
public class AuthenticationResource {
  @Inject AuthenticationService authenticationService;

  @POST
  @Path("/sign-in")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response signIn(AccountLoginDto accountLoginDto) {
    String response = authenticationService.signIn(accountLoginDto);

    return Response.ok(response).build();
  }

  @GET
  @Path("/verify")
  public Response verifyAccount(@QueryParam("token") String token) throws ParseException {
    authenticationService.verifyAccount(token);

    return Response.ok().build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/resend-verification-email")
  public Response resendAccountVerificationEmail(JsonObject payload)
      throws JsonProcessingException {
    authenticationService.resendAccountVerificationEmail(payload.getString("email"));

    return Response.ok().build();
  }

  @POST
  @Path("/forgot-password")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response forgotPassword(JsonObject payload) throws JsonProcessingException {
    authenticationService.sendPasswordResetEmail(payload.getString("email"));

    return Response.ok().build();
  }

  @GET
  @Path("/reset-password")
  public Response validatePasswordResetTokenAndRedirect(@QueryParam("token") String token)
      throws ParseException {
    String redirectUrl =
        authenticationService.validatePasswordResetTokenAndGenerateRedirectUrl(token);

    return Response.seeOther(URI.create(redirectUrl)).build();
  }

  @POST
  @Path("/reset-password")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response resetPassword(PasswordResetDto passwordResetDto) throws ParseException {
    authenticationService.resetPassword(passwordResetDto);

    return Response.ok().build();
  }
}
