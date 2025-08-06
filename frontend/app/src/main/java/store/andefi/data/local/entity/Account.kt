package store.andefi.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class Account(
    @PrimaryKey val id: String,
    @ColumnInfo("full_name") val fullName: String,
    val email: String,
    val username: String,
    @ColumnInfo("jwt_token") val jwtToken: String
)