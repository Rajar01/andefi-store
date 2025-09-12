package store.andefi.infrastructure.persistence.panache.seeder;

import com.github.javafaker.Faker;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import store.andefi.infrastructure.persistence.panache.entity.AccountPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.OrderItemPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.OrderPanacheEntity;
import store.andefi.infrastructure.persistence.panache.entity.ProductPanacheEntity;
import store.andefi.infrastructure.persistence.panache.repository.OrderRepositoryImpl;

import java.util.List;
import java.util.Locale;

@ApplicationScoped
@Priority(1)
public class OrderSeeder implements Seeder {
    @Inject
    OrderRepositoryImpl orderRepository;

    @Override
    public void run() {
        // If table already have data then just return
        boolean isEmpty = orderRepository.getEntityManager()
            .createQuery("FROM OrderPanacheEntity p ORDER BY p.id ASC", OrderPanacheEntity.class)
            .setMaxResults(1)
            .getResultList()
            .isEmpty();

        if (!isEmpty) return;

        Faker faker = new Faker(Locale.forLanguageTag("in-ID"));

        AccountPanacheEntity accountPanacheEntity = orderRepository.getEntityManager()
            .createQuery("FROM AccountPanacheEntity p ORDER BY p.id ASC", AccountPanacheEntity.class)
            .setMaxResults(1)
            .getResultList()
            .getFirst();

        List<ProductPanacheEntity> productPanacheEntities = orderRepository.getEntityManager()
            .createQuery("FROM ProductPanacheEntity p ORDER BY p.id ASC", ProductPanacheEntity.class)
            .setMaxResults(2)
            .getResultList();

        for (int i = 0; i < 50; i++) {
            OrderPanacheEntity order = OrderPanacheEntity.builder()
                .shippingAddress(accountPanacheEntity.getShippingAddress().getAddress())
                .shippingPostalCode(accountPanacheEntity.getShippingAddress().getPostalCode())
                .subTotal(0L)
                .totalDiscount(0L)
                .grandTotal(0L)
                .account(accountPanacheEntity)
                .build();

            OrderItemPanacheEntity orderItemPanacheEntity = OrderItemPanacheEntity.builder()
                .productName(productPanacheEntities.getFirst().getName())
                .productPrice(productPanacheEntities.getFirst().getPrice())
                .productMediaUrl(productPanacheEntities.getFirst().getMediaUrls().get("primary_image"))
                .quantity(0)
                .order(order)
                .product(productPanacheEntities.getFirst())
                .build();

            order.setOrderItems(List.of(orderItemPanacheEntity));

            orderRepository.persist(order);
        }

        for (int i = 0; i < 50; i++) {
            OrderPanacheEntity order = OrderPanacheEntity.builder()
                .shippingAddress(accountPanacheEntity.getShippingAddress().getAddress())
                .shippingPostalCode(accountPanacheEntity.getShippingAddress().getPostalCode())
                .subTotal(0L)
                .totalDiscount(0L)
                .grandTotal(0L)
                .account(accountPanacheEntity)
                .build();

            OrderItemPanacheEntity orderItemPanacheEntityOne = OrderItemPanacheEntity.builder()
                .productName(productPanacheEntities.getLast().getName())
                .productPrice(productPanacheEntities.getLast().getPrice())
                .productMediaUrl(productPanacheEntities.getLast().getMediaUrls().get("primary_image"))
                .quantity(0)
                .order(order)
                .product(productPanacheEntities.getLast())
                .build();

            OrderItemPanacheEntity orderItemPanacheEntityTwo = OrderItemPanacheEntity.builder()
                .productName(productPanacheEntities.getLast().getName())
                .productPrice(productPanacheEntities.getLast().getPrice())
                .productMediaUrl(productPanacheEntities.getLast().getMediaUrls().get("primary_image"))
                .quantity(0)
                .order(order)
                .product(productPanacheEntities.getLast())
                .build();

            order.setOrderItems(List.of(orderItemPanacheEntityOne, orderItemPanacheEntityTwo));

            orderRepository.persist(order);
        }
    }
}
