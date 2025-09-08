package store.andefi.core.service.implementation;

import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import store.andefi.core.entity.Account;
import store.andefi.core.entity.Order;
import store.andefi.core.exception.GeneratePaymentUrlException;
import store.andefi.core.service.AccountService;
import store.andefi.core.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {
    @Inject
    AccountService accountService;

    @ConfigProperty(name = "midtrans.server-key")
    String midtransServerKey;

    @ConfigProperty(name = "midtrans.is-production")
    boolean midtransIsProduction;

    @PostConstruct
    void initMidtransConfig() {
        Midtrans.serverKey = midtransServerKey;
        Midtrans.isProduction = midtransIsProduction;
    }

    @Override
    public String generatePaymentUrl(Order order) throws GeneratePaymentUrlException, MidtransError {
        // Credit card
        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("secure", "true");

        // Transaction details
        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", order.getId().toString());
        transactionDetails.put("gross_amount", String.valueOf(order.getGrandTotal()));

        // Customer details
        Account account = accountService.findAccountByIdOptional(order.getAccountId())
            .orElseThrow(() -> new GeneratePaymentUrlException(String.format("Account with id %s not found", order.getAccountId())));
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("email", account.getEmail());
        customerDetails.put("shipping_address", Map.of("address", account.getShippingAddress().getAddress()));

        Map<String, Object> params = new HashMap<>();
        params.put("transaction_details", transactionDetails);
        params.put("credit_card", creditCard);
        params.put("customer_details", customerDetails);

        return SnapApi.createTransactionRedirectUrl(params);
    }
}
