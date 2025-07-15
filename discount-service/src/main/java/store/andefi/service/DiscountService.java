package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import store.andefi.dto.DiscountDto;
import store.andefi.repository.DiscountRepository;
import store.andefi.utility.mapper.DiscountMapper;

import java.math.BigDecimal;
import java.util.UUID;

@ApplicationScoped
public class DiscountService {
    @Inject
    DiscountRepository discountRepository;

    public DiscountDto getProductDiscount(UUID productId) {
        return discountRepository.find("product_id", productId)
                .singleResultOptional().map(DiscountMapper::toDto).orElseThrow(NotFoundException::new);
    }
}
