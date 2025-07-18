package store.andefi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.AccountDto;
import store.andefi.dto.AccountRegisterDto;
import store.andefi.service.AccountService;

@Path("/api/accounts")
public class AccountResource {
  @Inject AccountService accountService;

  @GET
  @Path("/{account_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAccountById(@PathParam("account_id") String accountId) {
    AccountDto response = accountService.getAccountById(accountId);

    return Response.ok(response).build();
  }

  @POST
  @Path("/sign-up")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAccount(AccountRegisterDto accountRegisterDto) throws JsonProcessingException {
    accountService.createAccount(accountRegisterDto);

    return Response.status(Response.Status.CREATED).build();
  }
}
