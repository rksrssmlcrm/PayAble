package com.payable.app.presentation.onboarding.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.payable.app.R
import com.payable.app.presentation.navigation.Screen
import com.payable.app.presentation.onboarding.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val pages = listOf(
        OnboardingPage(
            title = "Take control of your loans",
            subtitle = "Check if you can afford a loan before applying",
            imageRes = R.drawable.wallet,
        ),
        OnboardingPage(
            title = "Instant affordability check",
            subtitle = "Enter your salary \u2192 see max amount & monthly payment",
            imageRes = R.drawable.calculator,
        ),
        OnboardingPage(
            title = "Compare real offers",
            subtitle = "Loans and microcredits from top companies",
            imageRes = R.drawable.offers,
        ),
        OnboardingPage(
            title = "Ready to borrow smart?",
            subtitle = "No hidden fees, honest calculations",
            imageRes = R.drawable.success,
        ),
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (pagerState.currentPage < pages.lastIndex) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.completeOnboarding()
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(0)
                                }
                            }
                        },
                    ) {
                        Text(text = "Skip")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) { page ->
                val item = pages[page]

                AnimatedVisibility(
                    visible = pagerState.currentPage == page,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(220.dp),
                            contentScale = ContentScale.Fit,
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(pages.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(if (isSelected) 20.dp else 8.dp)
                            .height(8.dp)
                            .clip(CircleShape)
                            .then(
                                if (isSelected) {
                                    Modifier
                                } else {
                                    Modifier
                                },
                            ),
                    ) {
                        Surface(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                            shape = CircleShape,
                            modifier = Modifier.fillMaxSize(),
                            content = {},
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val isLastPage = pagerState.currentPage == pages.lastIndex

            if (isLastPage) {
                Text(
                    text = "By continuing you agree to our ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                TextButton(
                    onClick = { navController.navigate(Screen.PrivacyPolicy.route) }
                ) {
                    Text(
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (isLastPage) {
                            viewModel.completeOnboarding()
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(0)
                            }
                        } else {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text(text = if (isLastPage) "Get Started" else "Next")
            }
        }
    }
}



