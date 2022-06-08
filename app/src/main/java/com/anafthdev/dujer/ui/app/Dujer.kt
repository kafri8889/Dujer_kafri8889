package com.anafthdev.dujer.ui.app

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.extension.toArray
import com.anafthdev.dujer.foundation.uimode.UiModeViewModel
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.app.component.CustomSnackbar
import com.anafthdev.dujer.ui.category.CategoryScreen
import com.anafthdev.dujer.ui.category.data.CategorySwipeAction
import com.anafthdev.dujer.ui.change_currency.ChangeCurrencyScreen
import com.anafthdev.dujer.ui.dashboard.DashboardScreen
import com.anafthdev.dujer.ui.income_expense.IncomeExpenseScreen
import com.anafthdev.dujer.ui.setting.SettingScreen
import com.anafthdev.dujer.ui.statistic.StatisticScreen
import com.anafthdev.dujer.ui.theme.DujerTheme
import com.anafthdev.dujer.ui.theme.black01
import com.anafthdev.dujer.ui.theme.black10
import com.anafthdev.dujer.ui.wallet.WalletScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
	ExperimentalMaterial3Api::class
)
@Composable
fun DujerApp() {
	
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	
	val dujerViewModel = hiltViewModel<DujerViewModel>()
	val uiModeViewModel = hiltViewModel<UiModeViewModel>()
	
	val state by dujerViewModel.state.collectAsState()
	val uiModeState by uiModeViewModel.state.collectAsState()
	
	val currentCurrency = state.currentCurrency
	val dataCanReturned = state.dataCanReturned
	val uiMode = uiModeState.uiMode
	
	val isSystemInDarkTheme = uiMode.isDarkTheme()
	
	val navController = rememberNavController()
	val systemUiController = rememberSystemUiController()
	val snackbarHostState = remember { SnackbarHostState() }
	
	val effect by dujerViewModel.effect.collectAsState(
		initial = DujerEffect.Nothing,
		context = Dispatchers.Main
	)
	
	LaunchedEffect(lifecycleOwner) {
		lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			dujerViewModel.dispatch(
				DujerAction.InsertWallet(Wallet.cash)
			)
		}
	}
	
	LaunchedEffect(effect) {
		when (effect) {
			is DujerEffect.DeleteFinancial -> {
				snackbarHostState.showSnackbar(
					message = "${context.getString(R.string.finance_removed)} \"${(effect as DujerEffect.DeleteFinancial).financial.name}\"",
					duration = SnackbarDuration.Short
				)
			}
			is DujerEffect.DeleteCategory -> {
				snackbarHostState.showSnackbar(
					message = "${context.getString(R.string.category_removed)} \"${(effect as DujerEffect.DeleteCategory).category.name}\"",
					duration = SnackbarDuration.Short
				)
			}
			else -> {}
		}
	}
	
	SideEffect {
		systemUiController.setSystemBarsColor(
			color = Color.Transparent,
			darkIcons = !isSystemInDarkTheme
		)
	}
	
	DujerTheme(
		isSystemInDarkTheme = isSystemInDarkTheme
	) {
		CompositionLocalProvider(
			LocalUiMode provides uiMode,
			LocalCurrency provides currentCurrency,
			LocalDujerState provides state,
			LocalContentColor provides if (isSystemInDarkTheme) black10 else black01,
			LocalOverScrollConfiguration provides null
		) {
			Scaffold(
				snackbarHost = {
					SnackbarHost(
						hostState = snackbarHostState,
						snackbar = { snackbarData ->
							CustomSnackbar(
								snackbarData = snackbarData,
								onCancel = {
									dujerViewModel.dispatch(
										DujerAction.Undo(dataCanReturned)
									)
								}
							)
						},
						modifier = Modifier
							.padding(
								vertical = 16.dpScaled,
								horizontal = 8.dpScaled
							)
					)
				}
			) {
				NavHost(
					navController = navController,
					startDestination = DujerDestination.Dashboard.route,
					modifier = Modifier
						.fillMaxSize()
				) {
					composable(DujerDestination.Dashboard.route) {
						DashboardScreen(
							navController = navController,
							dujerViewModel = dujerViewModel
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
							dujerViewModel = dujerViewModel
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
							onTransactionCanDelete = {
								dujerViewModel.dispatch(
									DujerAction.Vibrate(100)
								)
							},
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
				}
			}
		}
	}
}
