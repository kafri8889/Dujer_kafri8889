package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.category_transaction.CategoryTransactionScreen
import com.anafthdev.dujer.feature.category_transaction.CategoryTransactionViewModel

fun NavGraphBuilder.CategoryTransactionNavHost(
	navController: NavController,
	onDeleteTransaction: (Financial) -> Unit
) {
	navigation(
		startDestination = DujerDestination.CategoryTransaction.Home.route,
		route = DujerDestination.CategoryTransaction.Root.route
	) {
		composable(
			route = DujerDestination.CategoryTransaction.Home.route,
			arguments = DujerDestination.CategoryTransaction.Home.arguments
		) { backEntry ->
			
			val viewModel = hiltViewModel<CategoryTransactionViewModel>(backEntry)
			
			CategoryTransactionScreen(
				navController = navController,
				viewModel = viewModel,
				onDeleteTransaction = onDeleteTransaction
			)
		}
	}
}
