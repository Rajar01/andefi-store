package store.andefi.dto;

import java.util.List;
import lombok.Data;

@Data
public class EmailMessageDto {
  String from;
  List<String> to;
  String subject;
  String body;
}
