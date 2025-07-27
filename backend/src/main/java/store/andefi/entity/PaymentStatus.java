package store.andefi.entity;

public enum PaymentStatus {
  CAPTURE("capture"),
  SETTLEMENT("settlement"),
  PENDING("pending"),
  DENY("deny"),
  CANCEL("cancel"),
  EXPIRE("expire"),
  FAILURE("failure"),
  REFUND("refund"),
  PARTIAL_REFUND("partial_refund"),
  AUTHORIZE("authorize");

  private final String value;

  PaymentStatus(String value) {
    this.value = value;
  }

  public static PaymentStatus fromString(String value) {
    return switch (value) {
      case "capture" -> PaymentStatus.CAPTURE;
      case "settlement" -> PaymentStatus.SETTLEMENT;
      case "pending" -> PaymentStatus.PENDING;
      case "deny" -> PaymentStatus.DENY;
      case "cancel" -> PaymentStatus.CANCEL;
      case "expire" -> PaymentStatus.EXPIRE;
      case "failure" -> PaymentStatus.FAILURE;
      case "refund" -> PaymentStatus.REFUND;
      case "partial_refund" -> PaymentStatus.PARTIAL_REFUND;
      case "authorize" -> PaymentStatus.AUTHORIZE;
      default -> throw new IllegalArgumentException();
    };
  }

  @Override
  public String toString() {
    return this.value;
  }
}
