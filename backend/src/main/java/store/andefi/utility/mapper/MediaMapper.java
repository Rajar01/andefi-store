package store.andefi.utility.mapper;

import store.andefi.dto.MediaDto;
import store.andefi.entity.Media;

public final class MediaMapper {
  public static MediaDto toDto(Media mediaEntity) {
    MediaDto mediaDto = new MediaDto();
    mediaDto.setId(mediaEntity.getId().toString());
    mediaDto.setUrls(mediaEntity.getUrls());

    return mediaDto;
  }
}
