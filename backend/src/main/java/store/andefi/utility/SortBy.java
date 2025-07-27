package store.andefi.utility;

public enum SortBy {
  LATEST("latest"),
  RATING_ASC("rating_asc"),
  RATING_DESC("rating_desc");

  public static final String DEFAULT_SORT_BY = "latest";

  private final String value;

  SortBy(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
