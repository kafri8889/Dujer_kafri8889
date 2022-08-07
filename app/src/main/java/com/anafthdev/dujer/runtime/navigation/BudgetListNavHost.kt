package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.feature.budget_list.BudgetListScreen
import com.anafthdev.dujer.feature.budget_list.BudgetListViewModel

fun NavGraphBuilder.BudgetListNavHost(navController: NavController) {
	navigation(
		startDestination = DujerDestination.BudgetList.Home.route,
		route = DujerDestination.BudgetList.Root.route
	) {
		composable(DujerDestination.BudgetList.Home.route) { backEntry ->
			
			val viewModel = hiltViewModel<BudgetListViewModel>(backEntry)
			
			BudgetListScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
