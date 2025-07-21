package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.Instant;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@MongoEntity(collection = "shipping_addresses")
public record ShippingAddress(
    @BsonId ObjectId id,
    @BsonProperty("account_id") String accountId,
    String address,
    @BsonProperty("created_at") Instant createdAt,
    @BsonProperty("updated_at") Instant updatedAt) {}
