package store.andefi.utility

enum class SortBy(private val value: String) {
    LATEST("latest"),
    RATING_ASC("rating_asc"),
    RATING_DESC("rating_desc");

    override fun toString(): String {
        return this.value
    }

    companion object {
        const val DEFAULT_SORT_BY: String = "latest"
    }
}
