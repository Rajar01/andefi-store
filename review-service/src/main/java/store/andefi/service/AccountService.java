package store.andefi.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import store.andefi.dto.AccountDto;

@Path("/api/accounts")
@RegisterRestClient
public interface AccountService {
  @GET
  @Path("/{account_id}")
  @Produces(MediaType.APPLICATION_JSON)
  AccountDto getAccountById(@PathParam("account_id") String accountId);
}
