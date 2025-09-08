package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class OrderItemSummaryResponse {
    private UUID id;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_price")
    private Long productPrice;

    @JsonProperty("product_discount_percentage")
    private Integer productDiscountPercentage;

    @JsonProperty("product_media_url")
    private String productMediaUrl;

    private Integer quantity;
}
