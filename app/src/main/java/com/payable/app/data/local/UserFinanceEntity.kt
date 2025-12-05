package com.payable.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_finance")
data class UserFinanceEntity(
    @PrimaryKey val id: Int = 1,
    val monthlyIncome: Double,
    val monthlyExpenses: Double,
)









