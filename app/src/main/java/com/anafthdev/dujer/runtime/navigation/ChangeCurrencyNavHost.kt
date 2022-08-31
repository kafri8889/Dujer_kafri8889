package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.feature.change_currency.ChangeCurrencyScreen
import com.anafthdev.dujer.feature.change_currency.ChangeCurrencyViewModel

fun NavGraphBuilder.ChangeCurrencyNavHost(navController: NavController) {
	navigation(
		startDestination = DujerDestination.ChangeCurrency.Home.route,
		route = DujerDestination.ChangeCurrency.Root.route
	) {
		composable(DujerDestination.ChangeCurrency.Home.route) { backEntry ->
			
			val viewModel = hiltViewModel<ChangeCurrencyViewModel>(backEntry)
			
			ChangeCurrencyScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
