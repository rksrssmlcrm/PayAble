package com.payable.app.domain.model

data class Expense(
    val id: Int = 0,
    val amount: Double,
    val category: String,  // e.g. "Food", "Rent", "Salary" for income
    val date: Long = System.currentTimeMillis(),
    val isIncome: Boolean = false
)






