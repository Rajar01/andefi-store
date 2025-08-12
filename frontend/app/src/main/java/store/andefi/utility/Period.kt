package store.andefi.utility

enum class Period(private val value: String) {
    LAST_30_DAYS("last_30_days"),
    LAST_60_DAYS("last_60_days"),
    LAST_90_DAYS("last_90_days");

    override fun toString(): String {
        return this.value
    }
}
