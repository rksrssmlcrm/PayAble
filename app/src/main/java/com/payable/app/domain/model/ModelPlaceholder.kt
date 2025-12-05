package com.payable.app.domain.model

data class UserFinance(
    val monthlyIncome: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val id: Int = 1,
)

