package com.anafthdev.dujer.runtime.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.feature.category.CategoryScreen
import com.anafthdev.dujer.feature.category.CategoryViewModel

fun NavGraphBuilder.CategoryNavHost(
	navController: NavController,
	onDismissToEnd: (Category) -> Unit
) {
	navigation(
		startDestination = DujerDestination.Category.Home.route,
		route = DujerDestination.Category.Root.route
	) {
		composable(
			route = DujerDestination.Category.Home.route,
			arguments = DujerDestination.Category.Home.arguments
		) { backEntry ->
			
			val viewModel = hiltViewModel<CategoryViewModel>(backEntry)
			
			CategoryScreen(
				navController = navController,
				viewModel = viewModel,
				onDismissToEnd = onDismissToEnd
			)
		}
	}
}
