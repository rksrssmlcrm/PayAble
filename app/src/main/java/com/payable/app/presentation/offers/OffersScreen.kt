package com.payable.app.presentation.offers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.payable.app.presentation.offers.components.LoanOfferCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OffersScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val offers = OffersMockData.mockOffers

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Best offers for you") },
            )
        },
    ) { innerPadding ->
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                contentPadding = PaddingValues(12.dp),
            ) {
                items(offers, key = { it.id }) { offer ->
                    LoanOfferCard(
                        offer = offer,
                        navController = navController,
                        modifier = Modifier.animateItemPlacement(),
                    )
                }
            }
        }
    }
}


