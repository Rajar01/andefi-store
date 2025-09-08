package store.andefi.core.enums;

public enum TransactionStatus {
    CAPTURE("CAPTURE"),
    SETTLEMENT("SETTLEMENT"),
    PENDING("PENDING"),
    DENY("DENY"),
    CANCEL("CANCEL"),
    EXPIRE("EXPIRE"),
    FAILURE("FAILURE"),
    REFUND("REFUND"),
    PARTIAL_REFUND("PARTIAL_REFUND"),
    AUTHORIZE("AUTHORIZE");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
