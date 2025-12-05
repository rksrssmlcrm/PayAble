package com.payable.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [UserFinanceEntity::class, ExpenseEntity::class],
    version = 2,
    exportSchema = false,
)
// @TypeConverters(Converters::class) // Add when converters are needed
abstract class AppDatabase : RoomDatabase() {
    abstract fun userFinanceDao(): UserFinanceDao
    abstract fun expenseDao(): ExpenseDao
}


