package com.payable.app.presentation.offers.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.payable.app.domain.model.LoanOffer
import com.payable.app.domain.model.OfferType
import com.payable.app.presentation.navigation.Screen

@Composable
fun LoanOfferCard(
    offer: LoanOffer,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val accentColor = Color(offer.colorAccent)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.OfferDetails.createRoute(offer.id))
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = offer.logoRes,
                    contentDescription = offer.companyName,
                    tint = accentColor,
                    modifier = Modifier,
                )
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = offer.companyName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = when (offer.type) {
                                    OfferType.LOAN -> "Bank Loan"
                                    OfferType.MICROLOAN -> "Payday Loan"
                                },
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = accentColor.copy(alpha = 0.1f),
                            labelColor = accentColor,
                        ),
                    )
                }
            }

            Text(
                text = "up to $${offer.maxAmount.toLocaleCurrency()} • up to ${offer.maxTermMonths} months",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
            Text(
                text = "from ${offer.interestRate}% APR",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = accentColor,
            )
            if (offer.isFastApproval) {
                Text(
                    text = "Fast approval",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

private fun Int.toLocaleCurrency(): String {
    return String.format("%,d", this).replace(",", " ")
}



