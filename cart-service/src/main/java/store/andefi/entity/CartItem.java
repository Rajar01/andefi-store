package store.andefi.entity;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.hibernate.validator.constraints.UniqueElements;

public record CartItem(
    @UniqueElements @BsonProperty("product_id") String productId, Long quantity) {}
