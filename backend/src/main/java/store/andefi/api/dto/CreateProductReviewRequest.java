package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateProductReviewRequest {
    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("order_item_id")
    private UUID orderItemId;

    @JsonProperty("product_id")
    private UUID productId;

    private String content;

    private Integer rating;
}
