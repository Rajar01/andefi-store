package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@MongoEntity(collection = "products")
public record Product(
        @BsonId UUID id,
        @NotBlank String name,
        @NotBlank String description,
        @NotNull BigDecimal price,
        @BsonProperty("category_ids") List<String> categoryIds,
        Map<String, String> attributes,
        @BsonProperty("media_id") String mediaId,
        @BsonProperty("discount_id") String discountId,
        @NotNull @BsonProperty("created_at") Instant createdAt,
        @NotNull @BsonProperty("updated_at") Instant updatedAt
) {
}
