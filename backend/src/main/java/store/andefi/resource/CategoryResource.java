package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import store.andefi.dto.CategoryDto;
import store.andefi.service.CategoryService;

@Path("/api/categories")
public class CategoryResource {
  @Inject CategoryService categoryService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCategories() {
    List<CategoryDto> categoryDtos = categoryService.getCategories();

    return Response.ok(categoryDtos).build();
  }
}
