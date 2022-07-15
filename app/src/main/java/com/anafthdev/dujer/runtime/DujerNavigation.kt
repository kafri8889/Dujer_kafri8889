package com.anafthdev.dujer.runtime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.common.BottomSheetLayoutConfig
import com.anafthdev.dujer.foundation.extension.toArray
import com.anafthdev.dujer.ui.app.DujerAction
import com.anafthdev.dujer.ui.app.DujerViewModel
import com.anafthdev.dujer.ui.budget.BudgetScreen
import com.anafthdev.dujer.ui.budget_list.BudgetListScreen
import com.anafthdev.dujer.ui.category.CategoryScreen
import com.anafthdev.dujer.ui.category.data.CategorySwipeAction
import com.anafthdev.dujer.ui.category_transaction.CategoryTransactionScreen
import com.anafthdev.dujer.ui.change_currency.ChangeCurrencyScreen
import com.anafthdev.dujer.ui.dashboard.DashboardScreen
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.income_expense.IncomeExpenseScreen
import com.anafthdev.dujer.ui.setting.SettingScreen
import com.anafthdev.dujer.ui.statistic.StatisticScreen
import com.anafthdev.dujer.ui.wallet.WalletScreen
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DujerNavigation(
	dujerViewModel: DujerViewModel,
	modifier: Modifier = Modifier
) {
	
	val sheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true,
		animationSpec = SwipeableDefaults.AnimationSpec
	)
	
	val bottomSheetNavigator = remember(sheetState) {
		BottomSheetNavigator(sheetState = sheetState)
	}
	
	val navController = rememberNavController(bottomSheetNavigator)
	
	var bottomSheetLayoutConfig by remember { mutableStateOf(BottomSheetLayoutConfig()) }
	
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
			startDestination = DujerDestination.Dashboard.route,
			modifier = Modifier
				.fillMaxSize()
		) {
			composable(DujerDestination.BudgetList.route) {
				BudgetListScreen(
					navController = navController
				)
			}
			
			composable(DujerDestination.Setting.route) {
				SettingScreen(
					navController = navController
				)
			}
			
			composable(DujerDestination.Currency.route) {
				ChangeCurrencyScreen(
					navController = navController
				)
			}
			
			composable(DujerDestination.Dashboard.route) {
				DashboardScreen(
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
			}
			
			composable(
				route = DujerDestination.IncomeExpense.route,
				arguments = listOf(
					navArgument("type") {
						type = NavType.IntType
					}
				)
			) { entry ->
				val type = entry.arguments?.getInt("type") ?: 0
				
				IncomeExpenseScreen(
					navController = navController,
					type = FinancialType.values()[type],
					onDeleteTransaction = { financial ->
						dujerViewModel.dispatch(
							DujerAction.DeleteFinancial(
								financial.toArray()
							)
						)
					}
				)
			}
			
			composable(
				route = DujerDestination.Category.route,
				arguments = listOf(
					navArgument("id") {
						type = NavType.IntType
					},
					navArgument("action") {
						type = NavType.StringType
					}
				)
			) { entry ->
				val id = entry.arguments?.getInt("id") ?: Category.default.id
				val action = entry.arguments?.getString("action") ?: CategorySwipeAction.NOTHING
				
				CategoryScreen(
					id = id,
					action = action,
					navController = navController,
					dujerViewModel = dujerViewModel
				)
			}
			
			composable(
				route = DujerDestination.Wallet.route,
				arguments = listOf(
					navArgument("id") {
						type = NavType.IntType
					}
				)
			) { entry ->
				val id = entry.arguments?.getInt("id") ?: Wallet.cash.id
				
				WalletScreen(
					walletID = id,
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
			
			composable(
				route = DujerDestination.Statistic.route,
				arguments = listOf(
					navArgument("walletID") {
						type = NavType.IntType
					}
				)
			) { entry ->
				val walletID = entry.arguments?.getInt("walletID") ?: Wallet.cash.id
				
				StatisticScreen(
					walletID = walletID,
					navController = navController
				)
			}
			
			composable(
				route = DujerDestination.CategoryTransaction.route,
				arguments = listOf(
					navArgument("categoryID") {
						type = NavType.IntType
					}
				)
			) { entry ->
				val categoryID = entry.arguments?.getInt("categoryID") ?: Category.default.id
				
				CategoryTransactionScreen(
					categoryID = categoryID,
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
			
			composable(
				route = DujerDestination.Budget.route,
				arguments = listOf(
					navArgument("budgetID") {
						type = NavType.IntType
					}
				)
			) { entry ->
				val budgetID = entry.arguments?.getInt("budgetID") ?: Budget.defalut.id
				
				BudgetScreen(
					budgetID = budgetID,
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
			
			bottomSheet(
				route = DujerDestination.BottomSheet.Financial.route,
				arguments = listOf(
					navArgument(
						name = "action",
						builder = {
							type = NavType.StringType
						}
					),
					navArgument(
						name = "id",
						builder = {
							type = NavType.IntType
						}
					)
				)
			) { entry ->
				val action = entry.arguments?.getString("action") ?: FinancialAction.NEW
				val financialID = entry.arguments?.getInt("id") ?: Financial.default.id
				
				FinancialScreen(
					navController = navController,
					isScreenVisible = bottomSheetNavigator.navigatorSheetState.isVisible,
					financialID = financialID,
					financialAction = action
				)
			}
		}
	}
}
