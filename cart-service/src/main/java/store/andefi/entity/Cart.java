package store.andefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.Instant;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@MongoEntity(collection = "carts")
public record Cart(
    @BsonId ObjectId id,
    @BsonProperty("account_id") String accountId,
    @BsonProperty("cart_items") List<CartItem> cartItems,
    @BsonProperty("created_at") Instant createdAt,
    @BsonProperty("updated_at") Instant updatedAt) {}
