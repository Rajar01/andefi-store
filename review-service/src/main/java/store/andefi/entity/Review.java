package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@MongoEntity(collection = "reviews")
public record Review(
    @BsonId UUID id,
    @BsonProperty("product_id") String productId,
    @NotBlank String content,
    float rating,
    @BsonProperty("account_id") String accountId,
    @BsonProperty("media_id") String mediaId,
    @NotNull @BsonProperty("created_at") Instant createdAt,
    @NotNull @BsonProperty("updated_at") Instant updatedAt
) {}
