package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.ProductCatalogDto;
import store.andefi.dto.ProductDto;
import store.andefi.service.ProductService;

import java.util.UUID;

@Path("/api/products")
public class ProductResource {
    @Inject
    ProductService productService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductCatalog(@QueryParam("limit") @DefaultValue("10") int limit, @QueryParam("cursor") String cursor) {
        ProductCatalogDto response = productService.getProductCatalog(limit, cursor);

        return Response.ok(response).build();
    }

    @GET
    @Path("/{product_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("product_id") UUID productId) {
        ProductDto response = productService.getProductById(productId);

        return Response.ok(response).build();
    }
}
