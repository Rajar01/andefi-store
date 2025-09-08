package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CheckoutOrderItemResponse {
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_price")
    private Long productPrice;

    @JsonProperty("product_discount_percentage")
    private Integer productDiscountPercentage;

    @JsonProperty("media_url")
    private String mediaUrl;

    private Integer quantity;
}
