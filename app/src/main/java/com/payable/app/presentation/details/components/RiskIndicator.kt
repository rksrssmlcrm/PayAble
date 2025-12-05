package com.payable.app.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.payable.app.domain.usecase.RiskLevel

@Composable
fun RiskIndicator(
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier,
) {
    val (color, icon) = when (riskLevel) {
        RiskLevel.GREEN -> Color(0xFF00C853) to Icons.Filled.Check
        RiskLevel.YELLOW -> Color(0xFFFFC107) to Icons.Filled.Warning
        RiskLevel.RED -> Color(0xFFF44336) to Icons.Filled.ReportProblem
    }

    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(40.dp),
        )
    }
}


