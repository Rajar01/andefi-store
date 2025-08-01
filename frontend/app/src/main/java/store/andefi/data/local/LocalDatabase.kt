package store.andefi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import store.andefi.data.local.dao.AccountDao
import store.andefi.data.local.entity.Account

@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}