package com.payable.app.presentation.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payable.app.data.repository.ExpenseRepository
import com.payable.app.domain.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    private val _totalExpenses = MutableStateFlow(0.0)
    val totalExpenses: StateFlow<Double> = _totalExpenses.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        repository.getExpensesFlow()
            .onEach { expenses ->
                _expenses.value = expenses
            }
            .launchIn(viewModelScope)

        repository.getMonthlyTotalIncome()
            .onEach { income ->
                _totalIncome.value = income
            }
            .launchIn(viewModelScope)

        repository.getMonthlyTotalExpenses()
            .onEach { expenses ->
                _totalExpenses.value = expenses
            }
            .launchIn(viewModelScope)
    }

    fun addExpense(amount: String, category: String, isIncome: Boolean) {
        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _errorMessage.value = "Amount must be a positive number"
            return
        }

        if (category.isBlank()) {
            _errorMessage.value = "Please select a category"
            return
        }

        viewModelScope.launch {
            try {
                val expense = Expense(
                    amount = amountValue,
                    category = category,
                    isIncome = isIncome
                )
                repository.addExpense(expense)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add expense"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}






