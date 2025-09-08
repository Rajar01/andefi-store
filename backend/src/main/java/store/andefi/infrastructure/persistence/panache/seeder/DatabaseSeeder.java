package store.andefi.infrastructure.persistence.panache.seeder;

import io.quarkus.arc.All;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class DatabaseSeeder {
    @Inject
    @All
    List<Seeder> seeders;

    @Transactional
    void run(@Observes StartupEvent event) {
        for (Seeder seeder : seeders) {
            seeder.run();
        }
    }
}
