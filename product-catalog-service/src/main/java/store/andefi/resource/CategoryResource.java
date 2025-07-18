package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import store.andefi.dto.CategoryDto;
import store.andefi.service.CategoryService;

import java.util.List;

@Path("/api/categories")
public class CategoryResource {
    @Inject
    CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories() {
        List<CategoryDto> response = categoryService.getCategories();

        return Response.ok(response).build();
    }
}
