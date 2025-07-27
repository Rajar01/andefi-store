package store.andefi.utility.mapper;

import store.andefi.dto.PaymentDto;
import store.andefi.entity.Payment;

public final class PaymentMapper {
  public static PaymentDto toDto(Payment paymentEntity) {
    PaymentDto paymentDto = new PaymentDto();
    paymentDto.setId(paymentEntity.getId().toString());
    paymentDto.setAccountId(paymentEntity.getAccount().getId().toString());
    paymentDto.setOrderId(paymentEntity.getOrder().getId().toString());
    paymentDto.setMethod(paymentEntity.getMethod());
    paymentDto.setCurrency(paymentEntity.getCurrency());
    paymentDto.setAmount(paymentEntity.getAmount());
    paymentDto.setTransactionId(paymentEntity.getTransactionId());
    paymentDto.setTransactionTime(paymentEntity.getTransactionTime());
    paymentDto.setTransactionStatus(paymentEntity.getTransactionStatus());

    return paymentDto;
  }
}
