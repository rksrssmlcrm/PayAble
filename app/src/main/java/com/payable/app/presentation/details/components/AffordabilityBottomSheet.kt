package com.payable.app.presentation.details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.payable.app.domain.usecase.AffordabilityResult
import com.payable.app.domain.usecase.RiskLevel

@Composable
fun AffordabilityBottomSheet(
    result: AffordabilityResult,
    remainingBalances: List<Double>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val riskText = when (result.riskLevel) {
        RiskLevel.GREEN -> "You’re good!"
        RiskLevel.YELLOW -> "Be careful"
        RiskLevel.RED -> "Too risky"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = riskText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        AnimatedVisibility(
            visible = result.riskLevel == RiskLevel.GREEN,
            enter = fadeIn(animationSpec = tween(600)),
            exit = fadeOut(animationSpec = tween(600)),
        ) {
            RiskIndicator(
                riskLevel = result.riskLevel,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Monthly payment: $${formatCurrency(result.monthlyPayment)}",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "DTI: ${"%.1f".format(result.dtiRatio)}% of free income",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "Total interest: $${formatCurrency(result.totalInterest)}",
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Repayment schedule",
            style = MaterialTheme.typography.titleMedium,
        )

        PaymentChart(
            remainingBalances = remainingBalances,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Apply now")
        }
    }
}

private fun formatCurrency(value: Double): String =
    String.format("%,.0f", value).replace(',', ' ')


