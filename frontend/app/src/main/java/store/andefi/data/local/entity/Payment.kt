package store.andefi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment")
data class Payment(
    @PrimaryKey @ColumnInfo("order_id") val orderId: String,
    @ColumnInfo("redirect_url") val redirectUrl: String,
)