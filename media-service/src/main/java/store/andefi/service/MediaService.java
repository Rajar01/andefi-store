package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;
import store.andefi.dto.MediaDto;
chimport store.andefi.repository.MediaRepository;
import store.andefi.utility.mapper.MediaMapper;

@ApplicationScoped
public class MediaService {
  @Inject MediaRepository mediaRepository;

  public MediaDto getMediaById(String mediaId) {
    return mediaRepository
        .findByIdOptional(new ObjectId(mediaId))
        .map(MediaMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }
}
