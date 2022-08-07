package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.feature.setting.SettingScreen
import com.anafthdev.dujer.feature.setting.SettingViewModel

fun NavGraphBuilder.SettingNavHost(navController: NavController) {
	navigation(
		startDestination = DujerDestination.Setting.Home.route,
		route = DujerDestination.Setting.Root.route
	) {
		composable(DujerDestination.Setting.Home.route) { backEntry ->
			
			val viewModel = hiltViewModel<SettingViewModel>(backEntry)
			
			SettingScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
