package com.payable.app.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

data class AffordabilityResult(
    val monthlyPayment: Double,
    val totalPayment: Double,
    val totalInterest: Double,
    val dtiRatio: Double,
    val riskLevel: RiskLevel,
    val maxRecommendedAmount: Double,
)

enum class RiskLevel { GREEN, YELLOW, RED }

@Singleton
class CalculateAffordabilityUseCase @Inject constructor() {

    operator fun invoke(
        income: Double,
        expenses: Double,
        loanAmount: Double,
        termMonths: Int,
        ratePercent: Double,
    ): AffordabilityResult {
        val netIncome = (income - expenses).coerceAtLeast(0.0)
        val monthlyRate = ratePercent / 12.0 / 100.0

        val monthlyPayment = if (monthlyRate == 0.0) {
            loanAmount / termMonths
        } else {
            val pow = (1 + monthlyRate).pow(termMonths)
            loanAmount * monthlyRate * pow / (pow - 1)
        }

        val totalPayment = monthlyPayment * termMonths
        val totalInterest = totalPayment - loanAmount

        val dtiRatio = if (netIncome > 0) {
            (monthlyPayment / netIncome) * 100.0
        } else {
            100.0
        }

        val riskLevel = when {
            dtiRatio < 30.0 -> RiskLevel.GREEN
            dtiRatio <= 45.0 -> RiskLevel.YELLOW
            else -> RiskLevel.RED
        }

        val maxRecommendedAmount = netIncome * 0.35 * termMonths

        return AffordabilityResult(
            monthlyPayment = monthlyPayment,
            totalPayment = totalPayment,
            totalInterest = totalInterest,
            dtiRatio = dtiRatio,
            riskLevel = riskLevel,
            maxRecommendedAmount = maxRecommendedAmount,
        )
    }
}









