package com.payable.app

import com.payable.app.domain.usecase.CalculateAffordabilityUseCase
import com.payable.app.domain.usecase.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculateAffordabilityUseCaseTest {

    private lateinit var useCase: CalculateAffordabilityUseCase

    @Before
    fun setup() {
        useCase = CalculateAffordabilityUseCase()
    }

    @Test
    fun `calculate affordability with valid inputs returns correct result`() {
        // Given
        val income = 5000.0
        val expenses = 2000.0
        val loanAmount = 10000.0
        val termMonths = 24
        val ratePercent = 10.0

        // When
        val result = useCase(income, expenses, loanAmount, termMonths, ratePercent)

        // Then
        assertEquals(467.50, result.monthlyPayment, 0.01)
        assertEquals(11220.0, result.totalPayment, 0.01)
        assertEquals(1220.0, result.totalInterest, 0.01)
        assertEquals(23.38, result.dtiRatio, 0.01)
        assertEquals(RiskLevel.GREEN, result.riskLevel)
    }

    @Test
    fun `calculate affordability with zero expenses returns green risk level`() {
        // Given
        val income = 4000.0
        val expenses = 0.0
        val loanAmount = 5000.0
        val termMonths = 12
        val ratePercent = 5.0

        // When
        val result = useCase(income, expenses, loanAmount, termMonths, ratePercent)

        // Then
        assertEquals(RiskLevel.GREEN, result.riskLevel)
        assertEquals(429.66, result.monthlyPayment, 0.01)
    }

    @Test
    fun `calculate affordability with high DTI ratio returns red risk level`() {
        // Given
        val income = 2000.0
        val expenses = 1000.0
        val loanAmount = 10000.0
        val termMonths = 24
        val ratePercent = 15.0

        // When
        val result = useCase(income, expenses, loanAmount, termMonths, ratePercent)

        // Then
        assertEquals(RiskLevel.RED, result.riskLevel)
        assert(result.dtiRatio > 45.0)
    }

    @Test
    fun `calculate affordability with zero interest rate returns simple division`() {
        // Given
        val income = 3000.0
        val expenses = 1000.0
        val loanAmount = 6000.0
        val termMonths = 12
        val ratePercent = 0.0

        // When
        val result = useCase(income, expenses, loanAmount, termMonths, ratePercent)

        // Then
        assertEquals(500.0, result.monthlyPayment, 0.01) // 6000 / 12
        assertEquals(6000.0, result.totalPayment, 0.01)
        assertEquals(0.0, result.totalInterest, 0.01)
    }

    @Test
    fun `calculate affordability with negative net income returns red risk level`() {
        // Given
        val income = 1000.0
        val expenses = 2000.0
        val loanAmount = 5000.0
        val termMonths = 24
        val ratePercent = 10.0

        // When
        val result = useCase(income, expenses, loanAmount, termMonths, ratePercent)

        // Then
        assertEquals(RiskLevel.RED, result.riskLevel)
        assertEquals(100.0, result.dtiRatio, 0.01)
    }
}