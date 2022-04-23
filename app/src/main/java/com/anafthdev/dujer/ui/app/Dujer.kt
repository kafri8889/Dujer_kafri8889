package com.anafthdev.dujer.ui.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.category.CategoryScreen
import com.anafthdev.dujer.ui.category.data.CategoryAction
import com.anafthdev.dujer.ui.change_currency.ChangeCurrencyScreen
import com.anafthdev.dujer.ui.dashboard.DashboardScreen
import com.anafthdev.dujer.ui.income_expense.IncomeExpenseScreen
import com.anafthdev.dujer.ui.setting.SettingScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DujerApp() {
	
	val context = LocalContext.current
	
	val isSystemInDarkTheme = isSystemInDarkTheme()
	
	val dujerViewModel = hiltViewModel<DujerViewModel>()
	
	val state by dujerViewModel.state.collectAsState()
	
	val currentCurrency = state.currentCurrency
	
	val navController = rememberNavController()
	val systemUiController = rememberSystemUiController()
	
	SideEffect {
		systemUiController.setSystemBarsColor(
			color = Color.Transparent,
			darkIcons = isSystemInDarkTheme
		)
	}
	
	CompositionLocalProvider(
		LocalCurrency provides currentCurrency
	) {
		NavHost(
			navController = navController,
			startDestination = DujerDestination.Dashboard.route,
			modifier = Modifier
				.fillMaxSize()
		) {
			composable(DujerDestination.Dashboard.route) {
				DashboardScreen(
					navController = navController,
					dujerViewModel = dujerViewModel
				)
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
					type = FinancialType.values()[type],
					dujerViewModel = dujerViewModel
				)
			}
			
			composable(
				route = DujerDestination.Category.route,
				arguments = listOf(
					navArgument("id") {
						type = NavType.IntType
					},
					navArgument("action") {
						type = NavType.StringType
					}
				)
			) { entry ->
				val id = entry.arguments?.getInt("id") ?: Category.default.id
				val action = entry.arguments?.getString("action") ?: CategoryAction.NOTHING
				
				CategoryScreen(
					id = id,
					action = action,
					navController = navController,
					dujerViewModel = dujerViewModel
				)
			}
			
			composable(DujerDestination.Setting.route) {
				SettingScreen(
					navController = navController
				)
			}
			
			composable(DujerDestination.Currency.route) {
				ChangeCurrencyScreen(
					navController = navController
				)
			}
			
		}
	}
}
