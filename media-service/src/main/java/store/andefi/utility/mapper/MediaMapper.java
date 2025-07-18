package store.andefi.utility.mapper;

import store.andefi.dto.MediaDto;
import store.andefi.entity.Media;

public final class MediaMapper {
  public static MediaDto toDto(Media media) {
    return new MediaDto(media.id().toString(), media.urls());
  }
}
