package store.andefi.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import store.andefi.exception.DuplicationException;

@Provider
public class DuplicationExceptionMapper implements ExceptionMapper<DuplicationException> {

  @Override
  public Response toResponse(DuplicationException e) {
    return Response.status(Response.Status.CONFLICT).build();
  }
}
