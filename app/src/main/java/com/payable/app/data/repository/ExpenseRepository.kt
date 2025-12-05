package com.payable.app.data.repository

import com.payable.app.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun addExpense(expense: Expense)
    fun getExpensesFlow(): Flow<List<Expense>>
    fun getMonthlyTotalIncome(): Flow<Double>
    fun getMonthlyTotalExpenses(): Flow<Double>
}






