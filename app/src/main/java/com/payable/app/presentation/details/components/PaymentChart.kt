package com.payable.app.presentation.details.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.sp

@Composable
fun PaymentChart(
    remainingBalances: List<Double>,
    modifier: Modifier = Modifier,
) {
    if (remainingBalances.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Payment schedule chart\n(Coming soon)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
        return
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val maxBalance = remainingBalances.maxOrNull() ?: 0.0
        val minBalance = remainingBalances.minOrNull() ?: 0.0
        val balanceRange = maxBalance - minBalance

        if (balanceRange == 0.0) {
            // Draw empty chart
            return@Canvas
        }

        val points = remainingBalances.mapIndexed { index, balance ->
            val x = (index.toFloat() / (remainingBalances.size - 1)) * width
            val y = height - ((balance - minBalance) / balanceRange * height).toFloat()
            Offset(x, y)
        }

        // Draw line
        val path = Path().apply {
            if (points.isNotEmpty()) {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { lineTo(it.x, it.y) }
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF00C853),
            style = Stroke(width = 3f),
        )

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = Color(0xFF00C853),
                radius = 4f,
                center = point,
            )
        }
    }
}


