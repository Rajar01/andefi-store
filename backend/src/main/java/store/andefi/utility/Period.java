package store.andefi.utility;

public enum Period {
  LAST_30_DAYS("last_30_days"),
  LAST_60_DAYS("last_60_days"),
  LAST_90_DAYS("last_90_days");

  private final String value;

  Period(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
