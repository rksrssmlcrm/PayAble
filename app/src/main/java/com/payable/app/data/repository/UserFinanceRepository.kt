package com.payable.app.data.repository

import com.payable.app.domain.model.UserFinance
import kotlinx.coroutines.flow.Flow

interface UserFinanceRepository {
    suspend fun save(finance: UserFinance)
    fun getFinanceFlow(): Flow<UserFinance?>
}









