package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.StockDto;
import store.andefi.dto.StockUpdateDto;
import store.andefi.service.StockService;

@Path("/api")
public class StockResource {
    @Inject
    StockService stockService;

    @GET
    @Path("/products/{product_id}/stock")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductStock(@PathParam("product_id") String productId) {
        StockDto response = stockService.getProductStock(productId);

        return Response.ok(response).build();
    }

    @PATCH
    @Transactional
    @Path("/products/{product_id}/stock")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProductStock(StockUpdateDto updateStockDto) {
        stockService.updateProductStock(updateStockDto);

        return Response.ok().build();
    }
}
