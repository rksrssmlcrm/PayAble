package com.payable.app.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payable.app.data.local.OnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingDataStore: OnboardingDataStore,
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingDataStore.markAsShown()
        }
    }
}










