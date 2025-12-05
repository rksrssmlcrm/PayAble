package com.payable.app.presentation.myfinance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payable.app.data.repository.ExpenseRepository
import com.payable.app.data.repository.UserFinanceRepository
import com.payable.app.domain.model.UserFinance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class MyFinanceViewModel @Inject constructor(
    private val userFinanceRepository: UserFinanceRepository,
    private val expenseRepository: ExpenseRepository,
) : ViewModel() {

    private val _userFinance = MutableStateFlow(UserFinance())
    val userFinance: StateFlow<UserFinance> = _userFinance.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        combine(
            userFinanceRepository.getFinanceFlow(),
            expenseRepository.getMonthlyTotalIncome(),
            expenseRepository.getMonthlyTotalExpenses()
        ) { userFinance, expenseIncome, expenseExpenses ->
            userFinance?.copy(
                monthlyIncome = expenseIncome,
                monthlyExpenses = expenseExpenses
            ) ?: UserFinance(
                monthlyIncome = expenseIncome,
                monthlyExpenses = expenseExpenses
            )
        }
        .onEach { finance ->
            _userFinance.value = finance
        }
        .launchIn(viewModelScope)
    }

    fun saveIncome(income: String) {
        val value = income.toDoubleOrNull()
        if (value == null || value <= 0) {
            _errorMessage.value = "Income must be a positive number"
        } else {
            _userFinance.value = _userFinance.value.copy(monthlyIncome = value)
        }
    }

    fun saveExpenses(expenses: String) {
        val value = expenses.toDoubleOrNull()
        if (value == null || value < 0) {
            _errorMessage.value = "Expenses must be zero or more"
        } else {
            _userFinance.value = _userFinance.value.copy(monthlyExpenses = value)
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun persistFinance(onSaved: () -> Unit) {
        val current = _userFinance.value
        viewModelScope.launch {
            userFinanceRepository.save(current)
            onSaved()
        }
    }
}



