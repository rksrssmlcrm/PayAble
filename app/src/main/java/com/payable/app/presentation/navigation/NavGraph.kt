package com.payable.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.payable.app.presentation.dashboard.DashboardScreen
import com.payable.app.presentation.details.OfferDetailsScreen
import com.payable.app.presentation.expense.ExpenseTrackerScreen
import com.payable.app.presentation.myfinance.MyFinanceScreen
import com.payable.app.presentation.onboarding.components.OnboardingScreen
import com.payable.app.presentation.privacy.PrivacyPolicyScreen
import com.payable.app.presentation.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object PrivacyPolicy : Screen("privacy_policy")
    object Dashboard : Screen("dashboard")
    object MyFinance : Screen("my_finance")
    object ExpenseTracker : Screen("expense_tracker")
    object OfferDetails : Screen("offer_details/{offerId}") {
        fun createRoute(offerId: Int) = "offer_details/$offerId"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.MyFinance.route) {
            MyFinanceScreen(navController = navController)
        }
        composable(Screen.ExpenseTracker.route) {
            ExpenseTrackerScreen(navController = navController)
        }
        composable(Screen.OfferDetails.route) { backStackEntry ->
            val offerId = backStackEntry.arguments?.getString("offerId")?.toIntOrNull() ?: 0
            OfferDetailsScreen(offerId = offerId, navController = navController)
        }
    }
}

