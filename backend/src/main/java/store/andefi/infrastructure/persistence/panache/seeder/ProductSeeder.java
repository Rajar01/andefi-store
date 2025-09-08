package store.andefi.infrastructure.persistence.panache.seeder;

import com.github.f4b6a3.uuid.UuidCreator;
import com.github.javafaker.Faker;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import store.andefi.core.embedding.EmbeddingProvider;
import store.andefi.infrastructure.persistence.panache.entity.CategoryPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ProductPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.StockPanacheEntity;
import store.andefi.infrastructure.persistence.panache.repository.ProductRepositoryImpl;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
@Priority(1)
public class ProductSeeder implements Seeder {
    @Inject
    ProductRepositoryImpl productRepository;

    @Inject
    EmbeddingProvider embeddingProvider;

    @Override
    public void run() {
        // If table already have data then just return
        boolean isEmpty = productRepository.getEntityManager()
            .createQuery("FROM ProductPanacheEntity p ORDER BY p.id ASC", ProductPanacheEntity.class)
            .setMaxResults(1)
            .getResultList()
            .isEmpty();

        if (!isEmpty) return;

        Faker faker = new Faker(Locale.forLanguageTag("in-ID"));

        CategoryPanacheEntity categoryPanacheEntityOne = CategoryPanacheEntity.builder()
            .name(faker.funnyName().name())
            .build();

        productRepository.getEntityManager().persist(categoryPanacheEntityOne);

        CategoryPanacheEntity categoryPanacheEntityTwo = CategoryPanacheEntity.builder()
            .name(faker.funnyName().name())
            .build();

        productRepository.getEntityManager().persist(categoryPanacheEntityTwo);

        // Category one product
        for (int i = 0; i < 50; i++) {
            Map<String, String> mediaUrls = Map.of(
                "primary_image", "https://placehold.co/600x400/png"
            );

            StockPanacheEntity stockPanacheEntity = StockPanacheEntity.builder()
                .quantityOnHand(faker.number().numberBetween(100, 200))
                .build();

            ProductPanacheEntity productPanacheEntity = ProductPanacheEntity.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .name(faker.commerce().productName())
                .description(faker.lorem().paragraph(5))
                .price(faker.number().numberBetween(100000L, 300000L))
                .discountPercentage(faker.number().numberBetween(1, 9) * 10)
                .attributes(Map.of("Color", "Black", "Weight", "100kg"))
                .mediaUrls(mediaUrls)
                .stock(stockPanacheEntity)
                .categories(Set.of(categoryPanacheEntityOne))
                .build();

            // Create embedding for product name and description
            String input = String.format(
                "Product name:%s, product description:%s",
                productPanacheEntity.getName(), productPanacheEntity.getDescription()
            );
            float[][] embedding = embeddingProvider.generateEmbeddingText(input);
            productPanacheEntity.setEmbedding(embedding[0]);

            // Save product panache entity into database
            productRepository.persist(productPanacheEntity);
        }

        // Category two product
        for (int i = 0; i < 50; i++) {
            Map<String, String> mediaUrls = Map.of(
                "primary_image", "https://placehold.co/600x400/png"
            );

            StockPanacheEntity stockPanacheEntity = StockPanacheEntity.builder()
                .quantityOnHand(faker.number().numberBetween(100, 200))
                .build();

            ProductPanacheEntity productPanacheEntity = ProductPanacheEntity.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .name(faker.commerce().productName())
                .description(faker.lorem().paragraph(5))
                .price(faker.number().numberBetween(100000L, 300000L))
                .discountPercentage(faker.number().numberBetween(1, 9) * 10)
                .attributes(Map.of("Color", "Black", "Weight", "100kg"))
                .mediaUrls(mediaUrls)
                .stock(stockPanacheEntity)
                .categories(Set.of(categoryPanacheEntityTwo))
                .build();

            // Create embedding for product name and description
            String input = String.format(
                "Product name:%s, product description:%s",
                productPanacheEntity.getName(), productPanacheEntity.getDescription()
            );
            float[][] embedding = embeddingProvider.generateEmbeddingText(input);
            productPanacheEntity.setEmbedding(embedding[0]);

            // Save product panache entity into database
            productRepository.persist(productPanacheEntity);
        }
    }
}
