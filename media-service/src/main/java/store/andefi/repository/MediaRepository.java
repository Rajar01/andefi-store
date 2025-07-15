package store.andefi.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Media;

@ApplicationScoped
public class MediaRepository implements PanacheMongoRepository<Media> {}
