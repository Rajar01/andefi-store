package store.andefi.entity;

public enum OrderStatus {
  UNPAID("unpaid"),
  PAID("paid"),
  PACKED("packed"),
  SHIPPED("shipped"),
  COMPLETED("completed"),
  CANCELED("canceled");

  private final String value;

  OrderStatus(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
