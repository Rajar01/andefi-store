package store.andefi.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CheckoutOrderResponse {
    @JsonProperty("order_items")
    private List<CheckoutOrderItemResponse> orderItems;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_postal_code")
    private String shippingPostalCode;

    @JsonProperty("sub_total")
    private Long subTotal;

    @JsonProperty("grand_total")
    private Long grandTotal;

    @JsonProperty("total_discount")
    private Long totalDiscount;
}
