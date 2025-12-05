package com.payable.app.presentation.splash

import androidx.lifecycle.ViewModel
import com.payable.app.data.local.OnboardingDataStore
import com.payable.app.data.repository.UserFinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingDataStore: OnboardingDataStore,
    private val userFinanceRepository: UserFinanceRepository,
) : ViewModel() {

    suspend fun isOnboardingShown(): Boolean {
        return onboardingDataStore.onboardingShown.first()
    }

    suspend fun isIncomeZero(): Boolean {
        val finance = userFinanceRepository.getFinanceFlow().first()
        return finance?.monthlyIncome == null || finance.monthlyIncome == 0.0
    }
}


