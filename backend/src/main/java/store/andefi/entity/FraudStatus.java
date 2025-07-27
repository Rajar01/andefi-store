package store.andefi.entity;


public enum FraudStatus {
  ACCEPT("accept"),
  DENY("deny");

  private final String value;

  FraudStatus(String value) {
    this.value = value;
  }

  public static FraudStatus fromString(String value) {
    return switch (value) {
      case "accept" -> FraudStatus.ACCEPT;
      case "deny" -> FraudStatus.DENY;
      default -> throw new IllegalArgumentException();
    };
  }

  @Override
  public String toString() {
    return this.value;
  }
}
