package com.anafthdev.dujer.ui.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.currentFraction
import com.anafthdev.dujer.ui.screen.dashboard.DashboardScreen
import com.anafthdev.dujer.ui.screen.financial.FinancialScreen
import com.anafthdev.dujer.ui.screen.financial.FinancialViewModel
import com.anafthdev.dujer.ui.screen.income_expense.IncomeExpenseScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DujerApp() {
	
	val systemBarsBackground = MaterialTheme.colorScheme.background
	
	val dujerViewModel = hiltViewModel<DujerViewModel>()
	
	val isFinancialSheetShowed by dujerViewModel.isFinancialSheetShowed.observeAsState(initial = false)
	val financialID by dujerViewModel.financialID.observeAsState(initial = Financial.default.id)
	val financialAction by dujerViewModel.financialAction.observeAsState(
		initial = FinancialViewModel.FINANCIAL_ACTION_NEW
	)
	
	val scope = rememberCoroutineScope { Dispatchers.Main }
	val navController = rememberNavController()
	val systemUiController = rememberSystemUiController()
	val financialScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	Timber.i("isFinancialSheetShowed: $isFinancialSheetShowed")
	Timber.i("bs value: ${financialScreenSheetState.currentValue}")
	
	if (financialScreenSheetState.currentValue != ModalBottomSheetValue.Hidden) {
		DisposableEffect(Unit) {
			onDispose {
				dujerViewModel.hideFinancialSheet()
			}
		}
	}
	
	BackHandler(isFinancialSheetShowed) {
		dujerViewModel.hideFinancialSheet()
	}
	
	SideEffect {
		systemUiController.setSystemBarsColor(
			color = systemBarsBackground,
			darkIcons = true
		)
		
		scope.launch {
			if (isFinancialSheetShowed) financialScreenSheetState.show() else financialScreenSheetState.hide()
		}
	}
	
	ModalBottomSheetLayout(
		sheetState = financialScreenSheetState,
		scrimColor = Color.Unspecified,
		sheetContent = {
			FinancialScreen(
				financialID = financialID,
				financialAction = financialAction
			)
		}
	) {
		NavHost(
			navController = navController,
			startDestination = DujerDestination.Dashboard.route,
			modifier = Modifier
				.fillMaxSize()
		) {
			
			composable(DujerDestination.Dashboard.route) {
				DashboardScreen(navController = navController, dujerViewModel = dujerViewModel)
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
			
		}
	}
}
