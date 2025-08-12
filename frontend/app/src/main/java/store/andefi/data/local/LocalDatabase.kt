package store.andefi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import store.andefi.data.local.dao.AccountDao
import store.andefi.data.local.dao.PaymentDao
import store.andefi.data.local.entity.Account
import store.andefi.data.local.entity.Payment

@Database(entities = [Account::class, Payment::class], version = 2, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    abstract fun paymentDao(): PaymentDao
}