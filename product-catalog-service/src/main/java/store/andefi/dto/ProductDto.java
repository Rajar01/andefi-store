package store.andefi.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProductDto {
    String id;
    String name;
    String description;
    BigDecimal price;
    DiscountDto discount;
    List<String> categories;
    Map<String, String> attributes;
    MediaDto media;

    public ProductDto() {
    }

    public ProductDto(String id, String name, String description, BigDecimal price, DiscountDto discount, List<String> categories, Map<String, String> attributes, MediaDto media) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.categories = categories;
        this.attributes = attributes;
        this.media = media;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public DiscountDto getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountDto discount) {
        this.discount = discount;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public MediaDto getMedia() {
        return media;
    }

    public void setMedia(MediaDto media) {
        this.media = media;
    }
}


