package com.payable.app.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

enum class OfferType { LOAN, MICROLOAN }

data class LoanOffer(
    val id: Int,
    val companyName: String,
    val logoRes: ImageVector,
    val type: OfferType,
    val minAmount: Int,
    val maxAmount: Int,
    val minTermMonths: Int,
    val maxTermMonths: Int,
    val interestRate: Double,
    val isFastApproval: Boolean = false,
    val colorAccent: Long,
)



