package store.andefi.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Optional;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import store.andefi.dto.MediaDto;

@Path("/api/media")
@RegisterRestClient
public interface MediaService {
  @GET()
  @Path("/{media_id}")
  @Produces(MediaType.APPLICATION_JSON)
  MediaDto getMediaById(@PathParam("media_id") String mediaId);
}
