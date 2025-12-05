package com.payable.app.data.repository

import com.payable.app.data.local.UserFinanceDao
import com.payable.app.data.local.UserFinanceEntity
import com.payable.app.domain.model.UserFinance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserFinanceRepositoryImpl(
    private val dao: UserFinanceDao,
) : UserFinanceRepository {

    override suspend fun save(finance: UserFinance) {
        val entity = UserFinanceEntity(
            id = finance.id,
            monthlyIncome = finance.monthlyIncome,
            monthlyExpenses = finance.monthlyExpenses,
        )
        dao.save(entity)
    }

    override fun getFinanceFlow(): Flow<UserFinance?> {
        return dao.getFinance().map { entity ->
            entity?.let {
                UserFinance(
                    id = it.id,
                    monthlyIncome = it.monthlyIncome,
                    monthlyExpenses = it.monthlyExpenses,
                )
            }
        }
    }
}









