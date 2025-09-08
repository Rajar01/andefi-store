package store.andefi.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private UUID id;
    private String name;
    private String description;
    private Long price;
    private Integer discountPercentage;
    private Map<String, String> attributes;
    private Map<String, String> mediaUrls;
    private Stock stock;
    private Set<Category> categories;
}
