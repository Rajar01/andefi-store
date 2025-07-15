package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@MongoEntity(collection = "media")
public record Media(
    @BsonId ObjectId id,
    Map<String, String> urls,
    @NotNull @BsonProperty("created_at") Instant createdAt,
    @NotNull @BsonProperty("updated_at") Instant updatedAt
) {}
