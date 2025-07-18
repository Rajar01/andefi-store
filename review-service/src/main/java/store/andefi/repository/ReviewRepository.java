package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import store.andefi.entity.Review;

@ApplicationScoped
public class ReviewRepository implements PanacheMongoRepositoryBase<Review, UUID> {}
