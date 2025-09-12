package store.andefi.infrastructure.persistence.panache.seeder;

import com.github.f4b6a3.uuid.UuidCreator;
import com.github.javafaker.Faker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import store.andefi.infrastructure.persistence.panache.entity.AccountPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.OrderItemPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.OrderPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ReviewPanacheEntity;
import store.andefi.infrastructure.persistence.panache.repository.ReviewRepositoryImpl;

import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class ReviewSeeder implements Seeder {
    @Inject
    ReviewRepositoryImpl reviewRepository;

    @Override
    public void run() {
        // If table already have data then just return
        boolean isEmpty = reviewRepository.getEntityManager()
            .createQuery("FROM ReviewPanacheEntity p ORDER BY p.id ASC", ReviewPanacheEntity.class)
            .setMaxResults(1)
            .getResultList()
            .isEmpty();

        if (!isEmpty) return;

        Faker faker = new Faker(Locale.forLanguageTag("in-ID"));

        AccountPanacheEntity accountPanacheEntity = reviewRepository.getEntityManager()
            .createQuery("FROM AccountPanacheEntity p ORDER BY p.id ASC", AccountPanacheEntity.class)
            .setMaxResults(1)
            .getResultList()
            .getFirst();

        List<OrderPanacheEntity> orderPanacheEntities = reviewRepository.getEntityManager()
            .createQuery("FROM OrderPanacheEntity p ORDER BY p.id ASC", OrderPanacheEntity.class)
            .getResultList();

        for (OrderPanacheEntity orderPanacheEntity : orderPanacheEntities) {
            for (OrderItemPanacheEntity orderItemPanacheEntity : orderPanacheEntity.getOrderItems()) {
                ReviewPanacheEntity reviewPanacheEntity = ReviewPanacheEntity.builder()
                    .id(UuidCreator.getTimeOrderedEpoch())
                    .content(faker.bool().bool() ? faker.lorem().paragraph(faker.number().numberBetween(3, 5)) : null)
                    .rating(faker.number().numberBetween(1, 6))
                    .account(accountPanacheEntity)
                    .product(orderItemPanacheEntity.getProduct())
                    .orderItem(orderItemPanacheEntity)
                    .build();

                reviewRepository.persist(reviewPanacheEntity);
            }
        }
    }
}
