package com.payable.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFinanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(finance: UserFinanceEntity)

    @Query("SELECT * FROM user_finance WHERE id = 1 LIMIT 1")
    fun getFinance(): Flow<UserFinanceEntity?>
}









