package store.andefi.core.service;

import com.midtrans.httpclient.error.MidtransError;
import store.andefi.core.entity.Order;
import store.andefi.core.exception.GeneratePaymentUrlException;

public interface PaymentService {
    String generatePaymentUrl(Order order) throws GeneratePaymentUrlException, MidtransError;
}
