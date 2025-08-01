package store.andefi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import store.andefi.data.local.entity.Account

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountInformation(account: Account)

    @Update
    suspend fun updateAccountInformation(account: Account)

    @Query("SELECT * FROM account LIMIT 1")
    suspend fun getAccountInformation(): Account?
}
