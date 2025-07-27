package store.andefi.dto;

import java.util.Map;
import lombok.Data;

@Data
public class MediaDto {
  private String id;
  private Map<String, String> urls;
}
