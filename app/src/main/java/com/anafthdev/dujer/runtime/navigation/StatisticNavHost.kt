package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.feature.statistic.StatisticScreen
import com.anafthdev.dujer.feature.statistic.StatisticViewModel

fun NavGraphBuilder.StatisticNavHost(navController: NavController) {
	navigation(
		startDestination = DujerDestination.Statistic.Home.route,
		route = DujerDestination.Statistic.Root.route
	) {
		composable(
			route = DujerDestination.Statistic.Home.route,
			arguments = DujerDestination.Statistic.Home.arguments
		) { backEntry ->
			
			val viewModel = hiltViewModel<StatisticViewModel>(backEntry)
			
			StatisticScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
