package com.payable.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payable.app.data.repository.UserFinanceRepository
import com.payable.app.domain.model.LoanOffer
import com.payable.app.domain.model.UserFinance
import com.payable.app.domain.usecase.CalculateAffordabilityUseCase
import com.payable.app.presentation.offers.OffersMockData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: UserFinanceRepository,
    private val calculateAffordabilityUseCase: CalculateAffordabilityUseCase,
) : ViewModel() {

    private val _userFinance = MutableStateFlow(UserFinance())
    val userFinance: StateFlow<UserFinance> = _userFinance.asStateFlow()

    private val _maxSafeAmount = MutableStateFlow(0.0)
    val maxSafeAmount: StateFlow<Double> = _maxSafeAmount.asStateFlow()

    private val _recommendedOffers = MutableStateFlow<List<LoanOffer>>(emptyList())
    val recommendedOffers: StateFlow<List<LoanOffer>> = _recommendedOffers.asStateFlow()

    init {
        repository.getFinanceFlow()
            .onEach { finance ->
                if (finance != null) {
                    _userFinance.value = finance
                    recalculate(finance)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun recalculate(finance: UserFinance) {
        if (finance.monthlyIncome <= 0.0) {
            _maxSafeAmount.value = 0.0
            _recommendedOffers.value = emptyList()
            return
        }

        // Use base scenario: 36 months and average rate of 15%
        val baseTerm = 36
        val baseRate = 15.0
        val result = calculateAffordabilityUseCase(
            income = finance.monthlyIncome,
            expenses = finance.monthlyExpenses,
            loanAmount = 100_000.0, // doesn't matter, we use the maxRecommendedAmount formula
            termMonths = baseTerm,
            ratePercent = baseRate,
        )

        val maxSafe = result.maxRecommendedAmount
        _maxSafeAmount.value = maxSafe

        val minThreshold = maxSafe * 0.7
        _recommendedOffers.value = OffersMockData.mockOffers
            .filter { it.maxAmount.toDouble() >= minThreshold }
            .sortedBy { it.interestRate }
            .take(4)
    }
}

