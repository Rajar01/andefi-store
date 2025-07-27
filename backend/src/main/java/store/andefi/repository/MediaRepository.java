package store.andefi.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import store.andefi.entity.Media;

@ApplicationScoped
public class MediaRepository implements PanacheRepositoryBase<Media, UUID> {}
