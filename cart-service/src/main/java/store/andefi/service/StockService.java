package store.andefi.service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Optional;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import store.andefi.dto.StockDto;
import store.andefi.dto.StockUpdateDto;

@Path("/api")
@RegisterRestClient
public interface StockService {
  @GET
  @Path("/products/{product_id}/stock")
  @Produces(MediaType.APPLICATION_JSON)
  Optional<StockDto> getProductStock(@PathParam("product_id") String productId);

  @PATCH
  @Path("/products/{product_id}/stock")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  void updateProductStock(@PathParam("product_id") String productId, StockUpdateDto stockUpdateDto);
}
