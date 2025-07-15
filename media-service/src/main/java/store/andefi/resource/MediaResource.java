package store.andefi.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import store.andefi.dto.MediaDto;
import store.andefi.service.MediaService;

@Path("/api/media")
public class MediaResource {
  @Inject MediaService mediaService;

  @GET
  @Path("/{media_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMediaById(@PathParam("media_id") ObjectId mediaId) {
    MediaDto response = mediaService.getMediaById(mediaId);

    return Response.ok(response).build();
  }
}
