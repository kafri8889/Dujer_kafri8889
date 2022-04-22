package com.anafthdev.dujer.ui.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.anafthdev.dujer.ui.category.CategoryScreen
import com.anafthdev.dujer.ui.category.data.CategoryAction
import com.anafthdev.dujer.ui.dashboard.DashboardScreen
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.income_expense.IncomeExpenseScreen
import com.anafthdev.dujer.ui.setting.SettingScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DujerApp() {
	
	val context = LocalContext.current
	
	val isSystemInDarkTheme = isSystemInDarkTheme()
	
	val dujerViewModel = hiltViewModel<DujerViewModel>()
	
	val state by dujerViewModel.state.collectAsState()
	
	val navController = rememberNavController()
	val systemUiController = rememberSystemUiController()
	
	SideEffect {
		systemUiController.setSystemBarsColor(
			color = Color.Transparent,
			darkIcons = isSystemInDarkTheme
		)
	}
	
	Surface(
		color = MaterialTheme.colorScheme.background
	) {
	
	}
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
				navController = navController
			)
		}
		
		composable(DujerDestination.Setting.route) {
			SettingScreen(
				navController = navController
			)
		}
	}
}
