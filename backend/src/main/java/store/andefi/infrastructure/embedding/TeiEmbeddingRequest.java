package store.andefi.infrastructure.embedding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TeiEmbeddingRequest {
    @JsonProperty("inputs")
    String input;
}
