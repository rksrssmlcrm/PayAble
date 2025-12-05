package com.payable.app.data.repository

import com.payable.app.data.local.ExpenseDao
import com.payable.app.data.local.ExpenseEntity
import com.payable.app.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val dao: ExpenseDao,
) : ExpenseRepository {

    override suspend fun addExpense(expense: Expense) {
        val entity = ExpenseEntity(
            id = expense.id,
            amount = expense.amount,
            category = expense.category,
            date = expense.date,
            isIncome = expense.isIncome
        )
        dao.insert(entity)
    }

    override fun getExpensesFlow(): Flow<List<Expense>> {
        return dao.getAll().map { entities ->
            entities.map { entity ->
                Expense(
                    id = entity.id,
                    amount = entity.amount,
                    category = entity.category,
                    date = entity.date,
                    isIncome = entity.isIncome
                )
            }
        }
    }

    override fun getMonthlyTotalIncome(): Flow<Double> {
        return dao.getMonthlyTotalIncome()
    }

    override fun getMonthlyTotalExpenses(): Flow<Double> {
        return dao.getMonthlyTotalExpenses()
    }
}






