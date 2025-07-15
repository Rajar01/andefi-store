package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@MongoEntity(collection = "categories")
public record Category(
        @BsonId UUID id,
        @NotBlank String name,
        List<Category> children,
        @NotNull @BsonProperty("created_at") Instant createdAt,
        @NotNull @BsonProperty("updated_at") Instant updatedAt
) {
}
