package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.budget.BudgetScreen
import com.anafthdev.dujer.feature.budget.BudgetViewModel

fun NavGraphBuilder.BudgetNavHost(
	navController: NavController,
	onDeleteTransaction: (Financial) -> Unit
) {
	navigation(
		startDestination = DujerDestination.Budget.Home.route,
		route = DujerDestination.Budget.Root.route
	) {
		composable(
			route = DujerDestination.Budget.Home.route,
			arguments = DujerDestination.Budget.Home.arguments
		) { backEntry ->
			
			val viewModel = hiltViewModel<BudgetViewModel>(backEntry)
			
			BudgetScreen(
				navController = navController,
				viewModel = viewModel,
				onDeleteTransaction = onDeleteTransaction
			)
		}
	}
}
