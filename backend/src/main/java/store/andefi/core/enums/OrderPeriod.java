package store.andefi.core.enums;

public enum OrderPeriod {
  LAST_30_DAYS("LAST_30_DAYS"),
  LAST_60_DAYS("LAST_60_DAYS"),
  LAST_90_DAYS("LAST_90_DAYS");

  private final String value;

  OrderPeriod(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
