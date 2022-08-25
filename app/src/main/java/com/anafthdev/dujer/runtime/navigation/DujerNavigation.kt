package com.anafthdev.dujer.runtime.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.feature.app.DujerAction
import com.anafthdev.dujer.feature.app.DujerViewModel
import com.anafthdev.dujer.feature.app.LocalDujerState
import com.anafthdev.dujer.foundation.extension.toArray
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DujerNavigation(
	dujerViewModel: DujerViewModel,
	modifier: Modifier = Modifier
) {
	
	val dujerState = LocalDujerState.current
	
	val allTransaction = dujerState.allTransaction
	
	val sheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true,
		animationSpec = SwipeableDefaults.AnimationSpec
	)
	
	val bottomSheetNavigator = remember(sheetState) {
		BottomSheetNavigator(sheetState = sheetState)
	}
	val context = LocalContext.current
	
	val navController = rememberNavController(bottomSheetNavigator)
	
	ModalBottomSheetLayout(
		bottomSheetNavigator = bottomSheetNavigator,
		sheetShape = MaterialTheme.shapes.large.copy(
			bottomEnd = CornerSize(0),
			bottomStart = CornerSize(0)
		),
		modifier = modifier
	) {
		NavHost(
			navController = navController,
			startDestination = DujerDestination.Dashboard.Root.route,
			modifier = Modifier
				.fillMaxSize()
		) {
			BudgetListNavHost(navController = navController)
			
			SettingNavHost(navController = navController)
			
			ChangeCurrencyNavHost(navController = navController)
			
			DashboardNavHost(
				navController = navController,
				bottomSheetNavigator = bottomSheetNavigator,
				onDeleteTransaction = { financial ->
					dujerViewModel.dispatch(
						DujerAction.DeleteFinancial(
							financial.toArray()
						)
					)
				}
			)
			
			IncomeExpenseNavHost(
				navController = navController,
				onDeleteTransaction = { financial ->
					dujerViewModel.dispatch(
						DujerAction.DeleteFinancial(
							financial.toArray()
						)
					)
				}
			)
			
			CategoryNavHost(
				navController = navController,
				onDismissToEnd = { category, financials ->
					dujerViewModel.dispatch(
						DujerAction.DeleteCategory(
							category.toArray(),
							financials.toTypedArray()
						)
					)
				}
			)
			
			WalletNavHost(
				navController = navController,
				onDeleteTransaction = { financial ->
					dujerViewModel.dispatch(
						DujerAction.DeleteFinancial(
							financial.toArray()
						)
					)
				},
				onDeleteWallet = { wallet ->
					dujerViewModel.dispatch(
						DujerAction.DeleteWallet(
							wallet.toArray()
						)
					)
				}
			)
			
			StatisticNavHost(navController = navController)
			
			CategoryTransactionNavHost(
				navController = navController,
				onDeleteTransaction = { financial ->
					dujerViewModel.dispatch(
						DujerAction.DeleteFinancial(
							financial.toArray()
						)
					)
				}
			)
			
			BudgetNavHost(
				navController = navController,
				onDeleteTransaction = { financial ->
					dujerViewModel.dispatch(
						DujerAction.DeleteFinancial(
							financial.toArray()
						)
					)
				}
			)
		}
	}
}
