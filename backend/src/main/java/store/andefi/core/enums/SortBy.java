package store.andefi.core.enums;

public enum SortBy {
    LATEST("LATEST"),
    RATING_ASC("RATING_ASC"),
    RATING_DESC("RATING_DESC");

    public static final String DEFAULT_SORT_BY = "LATEST";

    private final String value;

    SortBy(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
