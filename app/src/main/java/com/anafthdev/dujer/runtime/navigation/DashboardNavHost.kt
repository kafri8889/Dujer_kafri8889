package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.dashboard.DashboardScreen
import com.anafthdev.dujer.feature.dashboard.DashboardViewModel
import com.anafthdev.dujer.feature.financial.FinancialScreen
import com.anafthdev.dujer.feature.financial.FinancialViewModel
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.DashboardNavHost(
	navController: NavController,
	bottomSheetNavigator: BottomSheetNavigator,
	onDeleteTransaction: (Financial) -> Unit
) {
	navigation(
		startDestination = DujerDestination.Dashboard.Home.route,
		route = DujerDestination.Dashboard.Root.route
	) {
		composable(DujerDestination.Dashboard.Home.route) { backEntry ->
			
			val viewModel = hiltViewModel<DashboardViewModel>(backEntry)
			
			DashboardScreen(
				navController = navController,
				viewModel = viewModel,
				bottomSheetNavigator = bottomSheetNavigator,
				onDeleteTransaction = onDeleteTransaction
			)
		}
		
		FinancialBottomSheet(
			navController = navController,
			isVisible = bottomSheetNavigator.navigatorSheetState.isVisible
		)
	}
}

@OptIn(ExperimentalMaterialNavigationApi::class)
private fun NavGraphBuilder.FinancialBottomSheet(
	navController: NavController,
	isVisible: Boolean
) {
	bottomSheet(
		route = DujerDestination.BottomSheet.Financial.Home.route,
		arguments = DujerDestination.BottomSheet.Financial.Home.arguments
	) {
		val viewModel = hiltViewModel<FinancialViewModel>()
		
		FinancialScreen(
			isScreenVisible = isVisible,
			navController = navController,
			viewModel = viewModel
		)
	}
}
