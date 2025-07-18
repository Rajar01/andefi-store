package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.Instant;
import java.util.List;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@MongoEntity(collection = "accounts")
public record Account(
    @BsonId ObjectId id,
    String email,
    String username,
    String password,
    @BsonProperty("role_ids") List<String> roleIds,
    @BsonProperty("is_verified") boolean isVerified,
    @BsonProperty("verified_at") Instant verifiedAt,
    @BsonProperty("created_at") Instant createdAt,
    @BsonProperty("updated_at") Instant updatedAt) {}
