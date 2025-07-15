package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;
import java.util.UUID;

@MongoEntity(collection = "discounts")
public record Discount(
        @BsonId UUID id,
        @NotNull @BsonProperty("product_id") UUID productId,
        @BsonProperty("discount_percentage") Long discountPercentage,
        @BsonProperty("is_active") boolean isActive,
        @BsonProperty("start_date") Instant startDate,
        @BsonProperty("end_date") Instant endDate,
        @NotNull @BsonProperty("created_at") Instant createdAt,
        @NotNull @BsonProperty("updated_at") Instant updatedAt
) {
}
