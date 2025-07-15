package store.andefi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;
import store.andefi.dto.DiscountDto;
import store.andefi.repository.DiscountRepository;
import store.andefi.utility.mapper.DiscountMapper;

@ApplicationScoped
public class DiscountService {
    @Inject
    DiscountRepository discountRepository;

    public DiscountDto getDiscountById(ObjectId discountId) {
        return discountRepository.findByIdOptional(discountId)
                .map(DiscountMapper::toDto).orElseThrow(NotFoundException::new);
    }
}
