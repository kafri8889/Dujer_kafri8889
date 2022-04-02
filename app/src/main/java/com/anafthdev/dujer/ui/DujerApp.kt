package com.anafthdev.dujer.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.ui.screen.dashboard.DashboardScreen
import com.anafthdev.dujer.ui.screen.income.IncomeScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DujerApp() {
	
	val navController = rememberNavController()
	val systemUiController = rememberSystemUiController()
	
	SideEffect {
		systemUiController.setSystemBarsColor(
			color = Color.Transparent,
			darkIcons = true
		)
	}
	
	NavHost(
		navController = navController,
		startDestination = DujerDestination.Dashboard.route
	) {
		
		composable(DujerDestination.Dashboard.route) {
			DashboardScreen(navController = navController)
		}
		
		composable(DujerDestination.Income.route) {
			IncomeScreen(navController = navController)
		}
	}
}
