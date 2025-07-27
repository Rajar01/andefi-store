package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.ProductDto;
import store.andefi.dto.ProductsDto;
import store.andefi.service.ProductService;

@Path("/api/products")
public class ProductResource {
  @Inject ProductService productService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProducts(
      @QueryParam("category") @DefaultValue("") String category,
      @QueryParam("limit") @DefaultValue("10") int limit,
      @QueryParam("cursor") String cursor) {
    ProductsDto productsDto = productService.getProducts(category, limit, cursor);

    return Response.ok(productsDto).build();
  }

  @GET
  @Path("/{product_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProductById(@PathParam("product_id") String productId) {
    ProductDto productDto = productService.getProductById(productId);

    return Response.ok(productDto).build();
  }

  @GET
  @Path("/search")
  @Produces(MediaType.APPLICATION_JSON)
  public Response search(
      @QueryParam("keyword") @DefaultValue("") String keyword,
      @QueryParam("limit") @DefaultValue("10") int limit,
      @QueryParam("cursor") String cursor) {
    ProductsDto productsDto = productService.search(keyword, limit, cursor);

    return Response.ok(productsDto).build();
  }
}
