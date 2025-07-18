package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;

@MongoEntity(collection = "categories")
public record Category(
        @BsonId ObjectId id,
        @NotBlank String name,
        @NotNull @BsonProperty("created_at") Instant createdAt,
        @NotNull @BsonProperty("updated_at") Instant updatedAt
) {
}
