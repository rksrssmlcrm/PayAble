package com.payable.app.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payable.app.data.repository.UserFinanceRepository
import com.payable.app.domain.model.LoanOffer
import com.payable.app.domain.usecase.AffordabilityResult
import com.payable.app.domain.usecase.CalculateAffordabilityUseCase
import com.payable.app.presentation.offers.OffersMockData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class OfferDetailsViewModel @Inject constructor(
    private val calculateAffordabilityUseCase: CalculateAffordabilityUseCase,
    private val userFinanceRepository: UserFinanceRepository,
) : ViewModel() {

    private val _selectedOffer = MutableStateFlow<LoanOffer?>(null)
    val selectedOffer: StateFlow<LoanOffer?> = _selectedOffer.asStateFlow()

    private val _calculationResult = MutableStateFlow<AffordabilityResult?>(null)
    val calculationResult: StateFlow<AffordabilityResult?> = _calculationResult.asStateFlow()

    fun loadOffer(offerId: Int) {
        if (_selectedOffer.value != null) return
        _selectedOffer.value = OffersMockData.mockOffers.firstOrNull { it.id == offerId }
    }

    fun calculate(loanAmount: Double, termMonths: Int) {
        val offer = _selectedOffer.value ?: return
        viewModelScope.launch {
            val finance = userFinanceRepository.getFinanceFlow().first()
            val result = calculateAffordabilityUseCase(
                income = finance?.monthlyIncome ?: 0.0,
                expenses = finance?.monthlyExpenses ?: 0.0,
                loanAmount = loanAmount,
                termMonths = termMonths,
                ratePercent = offer.interestRate,
            )
            _calculationResult.value = result
        }
    }
}


