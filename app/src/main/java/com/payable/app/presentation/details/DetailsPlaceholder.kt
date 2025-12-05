package com.payable.app.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.payable.app.presentation.details.components.AffordabilityBottomSheet
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferDetailsScreen(
    offerId: Int,
    navController: NavHostController,
    viewModel: OfferDetailsViewModel = hiltViewModel(),
) {
    val offer by viewModel.selectedOffer.collectAsState()
    val result by viewModel.calculationResult.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showSheet = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(offerId) {
        viewModel.loadOffer(offerId)
    }

    val currentOffer = offer ?: return

    val amountRange = currentOffer.minAmount.toFloat()..currentOffer.maxAmount.toFloat()
    val termRange = currentOffer.minTermMonths.toFloat()..currentOffer.maxTermMonths.toFloat()

    val selectedAmount = remember { mutableStateOf(currentOffer.maxAmount.toFloat()) }
    val selectedTerm = remember { mutableStateOf(currentOffer.minTermMonths.toFloat()) }

    val remainingBalances = remember(result) {
        val currentResult = result
        if (currentResult == null) emptyList() else {
            // simple linear approximation of remaining balance
            val step = currentResult.totalPayment / selectedTerm.value
            List(selectedTerm.value.toInt() + 1) { index ->
                val paid = step * index
                (currentResult.totalPayment - paid).coerceAtLeast(0.0)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = currentOffer.companyName) },
            )
        },
    ) { innerPadding ->
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Column {
            Text(
                text = "Up to $${currentOffer.maxAmount} at ${currentOffer.interestRate}%",
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Loan amount: $${selectedAmount.value.roundToInt()}")
            Slider(
                value = selectedAmount.value,
                onValueChange = { selectedAmount.value = it },
                valueRange = amountRange,
                steps = ((amountRange.endInclusive - amountRange.start) / 10_000f).toInt(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Term: ${selectedTerm.value.roundToInt()} months")
            Slider(
                value = selectedTerm.value,
                onValueChange = { selectedTerm.value = it },
                valueRange = termRange,
                steps = (termRange.endInclusive - termRange.start).toInt(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.calculate(
                        loanAmount = selectedAmount.value.toDouble(),
                        termMonths = selectedTerm.value.roundToInt(),
                    )
                    scope.launch {
                        showSheet.value = true
                        sheetState.show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(text = "Check if I can afford it")
            }
            }
        }
    }

    if (showSheet.value && result != null) {
        ModalBottomSheet(
            onDismissRequest = { showSheet.value = false },
            sheetState = sheetState,
        ) {
            AffordabilityBottomSheet(
                result = result!!,
                remainingBalances = remainingBalances,
            )
        }
    }
}

