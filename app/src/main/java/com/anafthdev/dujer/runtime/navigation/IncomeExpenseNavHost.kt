package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.income_expense.IncomeExpenseScreen
import com.anafthdev.dujer.feature.income_expense.IncomeExpenseViewModel

fun NavGraphBuilder.IncomeExpenseNavHost(
	navController: NavController,
	onDeleteTransaction: (Financial) -> Unit
) {
	navigation(
		startDestination = DujerDestination.IncomeExpense.Home.route,
		route = DujerDestination.IncomeExpense.Root.route
	) {
		composable(
			route = DujerDestination.IncomeExpense.Home.route,
			arguments = DujerDestination.IncomeExpense.Home.arguments
		) { backEntry ->
			
			val viewModel = hiltViewModel<IncomeExpenseViewModel>(backEntry)
			
			IncomeExpenseScreen(
				navController = navController,
				viewModel = viewModel,
				onDeleteTransaction = onDeleteTransaction
			)
		}
	}
}
