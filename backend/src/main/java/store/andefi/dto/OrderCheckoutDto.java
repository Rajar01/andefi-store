package store.andefi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderCheckoutDto {
    @JsonProperty("order_items")
    List<OrderItemCheckoutDto> orderItem;
}
