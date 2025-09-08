package store.andefi.core.enums;

public enum OrderStatus {
    UNPAID("UNPAID"),
    PAID("PAID"),
    PACKED("PACKED"),
    SHIPPED("SHIPPED"),
    COMPLETED("COMPLETED"),
    CANCELED("CANCELED"),
    REFUNDED("REFUNDED"),
    PARTIAL_REFUNDED("PARTIAL_REFUNDED");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
