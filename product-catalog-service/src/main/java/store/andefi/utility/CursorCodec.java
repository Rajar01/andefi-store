package store.andefi.utility;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CursorCodec {
  public static String encode(String cursor) {
    return Base64.getEncoder().encodeToString(cursor.getBytes(StandardCharsets.UTF_8));
  }

  public static String decode(String cursor) {
    return new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
  }
}
