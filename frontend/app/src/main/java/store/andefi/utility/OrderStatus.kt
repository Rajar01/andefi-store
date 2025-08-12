package store.andefi.utility

enum class OrderStatus(private val value: String) {
    UNPAID("unpaid"),
    PAID("paid"),
    PACKED("packed"),
    SHIPPED("shipped"),
    COMPLETED("completed"),
    CANCELED("canceled");

    override fun toString(): String {
        return this.value
    }
}
