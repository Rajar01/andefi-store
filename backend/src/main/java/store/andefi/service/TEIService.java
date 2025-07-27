package store.andefi.service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import store.andefi.dto.TEIDto;

@RegisterRestClient
public interface TEIService {
  @POST
  @Path("/embed")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  float[][] getEmbedding(TEIDto teiDto);
}
