package store.andefi.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import store.andefi.entity.Payment;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<Payment, UUID> {
}
