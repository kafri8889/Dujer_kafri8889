package com.anafthdev.dujer.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.ui.screen.dashboard.DashboardScreen
import com.anafthdev.dujer.ui.screen.income_expense.IncomeExpenseScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DujerApp() {
	
	val systemBarsBackground = MaterialTheme.colorScheme.background
	
	val navController = rememberNavController()
	val systemUiController = rememberSystemUiController()
	
	SideEffect {
		systemUiController.setSystemBarsColor(
			color = systemBarsBackground,
			darkIcons = true
		)
	}
	
	NavHost(
		navController = navController,
		startDestination = DujerDestination.Dashboard.route
	) {
		
		composable(DujerDestination.Dashboard.route) {
			DashboardScreen(navController = navController)
		}
		
		composable(
			route = DujerDestination.IncomeExpense.route,
			arguments = listOf(
				navArgument("type") {
					type = NavType.IntType
				}
			)
		) { entry ->
			val type = entry.arguments?.getInt("type") ?: 0
			
			IncomeExpenseScreen(
				navController = navController,
				type = FinancialType.values()[type]
			)
		}
	}
}
