package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.Instant;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@MongoEntity(collection = "roles")
public record Role(
    @BsonId ObjectId id,
    String name,
    @BsonProperty("created_at") Instant createdAt,
    @BsonProperty("updated_at") Instant updatedAt) {}
