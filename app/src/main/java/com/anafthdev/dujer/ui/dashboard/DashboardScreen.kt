package com.anafthdev.dujer.ui.dashboard

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.merge
import com.anafthdev.dujer.foundation.extension.toArray
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.app.DujerAction
import com.anafthdev.dujer.ui.app.DujerViewModel
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.chart.ChartScreen
import com.anafthdev.dujer.ui.dashboard.component.*
import com.anafthdev.dujer.ui.dashboard.subscreen.AddWalletScreen
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.search.SearchScreen
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.expenseColor
import com.anafthdev.dujer.ui.theme.incomeColor
import com.anafthdev.dujer.ui.theme.shapes
import com.anafthdev.dujer.uicomponent.BudgetCard
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val navigationRailItem = listOf(
	R.string.dashboard to R.drawable.ic_dashboard,
	R.string.chart to R.drawable.ic_chart,
	R.string.export to R.drawable.ic_export
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
	navController: NavController,
	dujerViewModel: DujerViewModel
) {
	
	val context = LocalContext.current
	
	val dashboardViewModel = hiltViewModel<DashboardViewModel>()
	
	val scope = rememberCoroutineScope()
	val financialScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val state by dashboardViewModel.state.collectAsState()
	val financial = state.financial
	val financialAction = state.financialAction
	
	DisposableEffect(financialScreenSheetState.isVisible) {
		onDispose {
			if (!financialScreenSheetState.isVisible) {
				dashboardViewModel.dispatch(
					DashboardAction.SetFinancialID(Financial.default.id)
				)
				
				dashboardViewModel.dispatch(
					DashboardAction.SetFinancialAction(FinancialAction.NEW)
				)
			}
		}
	}
	
	BackHandler(enabled = financialScreenSheetState.isVisible) {
		scope.launch {
			financialScreenSheetState.hide()
		}
	}
	
	ModalBottomSheetLayout(
		scrimColor = Color.Unspecified,
		sheetState = financialScreenSheetState,
		sheetContent = {
			FinancialScreen(
				isScreenVisible = financialScreenSheetState.isVisible,
				financial = financial,
				financialAction = financialAction,
				onBack = {
					scope.launch {
						financialScreenSheetState.hide()
					}
				},
				onSave = {
					scope.launch {
						financialScreenSheetState.hide()
					}
				}
			)
		}
	) {
		SearchScreen(
			navController = navController,
			canBack = !financialScreenSheetState.isVisible,
			content = {
				DashboardContent(
					financialScreenSheetState = financialScreenSheetState,
					dujerViewModel = dujerViewModel,
					dashboardViewModel = dashboardViewModel,
					navController = navController,
					onSettingClicked = {
						navController.navigate(DujerDestination.Setting.route) {
							navController.graph.startDestinationRoute?.let { startRoute ->
								popUpTo(startRoute) {
									saveState = true
								}
							}
							
							restoreState = true
							launchSingleTop = true
						}
					}
				)
			},
			onFinancialClicked = { clickedFinancial ->
				dashboardViewModel.dispatch(
					DashboardAction.SetFinancialAction(FinancialAction.EDIT)
				)
				
				dashboardViewModel.dispatch(
					DashboardAction.SetFinancialID(clickedFinancial.id)
				)
				
				scope.launch {
					financialScreenSheetState.show()
				}
			}
		)
	}
	
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
	ExperimentalAnimationApi::class
)
@Composable
private fun DashboardContent(
	financialScreenSheetState: ModalBottomSheetState,
	dujerViewModel: DujerViewModel,
	dashboardViewModel: DashboardViewModel,
	navController: NavController,
	onSettingClicked: () -> Unit
) {
	
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	
	val wallets = dujerState.allWallet
	val incomeTransaction = dujerState.allIncomeTransaction
	val expenseTransaction = dujerState.allExpenseTransaction
	
	val scope = rememberCoroutineScope()
	val dashboardNavController = rememberAnimatedNavController()
	
	val currentRoute = dashboardNavController.currentDestination?.route
	
	val incomeLineDataset by remember {
		mutableStateOf(
			LineDataSet(
				emptyList(),
				context.getString(R.string.income)
			).apply {
				lineWidth = 2.5f
				cubicIntensity = .2f
				mode = LineDataSet.Mode.CUBIC_BEZIER
				color = incomeColor.toArgb()
				setDrawValues(false)
				setDrawFilled(false)
				setDrawCircles(false)
				setCircleColor(incomeColor.toArgb())
				setDrawHorizontalHighlightIndicator(false)
			}
		)
	}
	
	val expenseLineDataset by remember {
		mutableStateOf(
			LineDataSet(
				emptyList(),
				context.getString(R.string.expenses)
			).apply {
				lineWidth = 2.5f
				cubicIntensity = .2f
				mode = LineDataSet.Mode.CUBIC_BEZIER
				color = expenseColor.toArgb()
				setDrawValues(false)
				setDrawFilled(false)
				setDrawCircles(false)
				setCircleColor(expenseColor.toArgb())
				setDrawHorizontalHighlightIndicator(false)
			}
		)
	}
	
	val showFinancialSheet = {
		scope.launch { financialScreenSheetState.show() }
		Unit
	}
	
	val hideFinancialSheet = {
		scope.launch { financialScreenSheetState.hide() }
		Unit
	}
	
	val vibrate = {
		dujerViewModel.dispatch(
			DujerAction.Vibrate(100)
		)
	}
	
	var showNavRail by rememberSaveable { mutableStateOf(false) }
	var showFABNewTransaction by rememberSaveable { mutableStateOf(true) }
	var selectedNavRailItem by rememberSaveable { mutableStateOf(navigationRailItem[0]) }
	
	val menuIconPadding by animateDpAsState(
		targetValue = if (showNavRail) 16.dpScaled else 0.dpScaled,
		animationSpec = tween(400)
	)
	
	LaunchedEffect(incomeTransaction, expenseTransaction) {
		val (incomeEntry, expenseEntry) = dashboardViewModel.getLineDataSetEntry(
			incomeList = incomeTransaction,
			expenseList = expenseTransaction
		)
		
		incomeLineDataset.values = incomeEntry
		expenseLineDataset.values = expenseEntry
	}
	
	BackHandler {
		when {
			financialScreenSheetState.isVisible -> hideFinancialSheet()
			showNavRail -> showNavRail = false
			currentRoute != DujerDestination.Dashboard.Home.route -> {
				selectedNavRailItem = navigationRailItem[0]
				popupDashboardNavigation(
					toRoute = DujerDestination.Dashboard.Home.route,
					dashboardNavController = dashboardNavController
				)
			}
			else -> (context as Activity).finish()
		}
	}
	
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		AnimatedVisibility(
			visible = showFABNewTransaction,
			enter = scaleIn(
				animationSpec = tween(200)
			),
			exit = scaleOut(
				animationSpec = tween(200)
			),
			modifier = Modifier
				.padding(32.dpScaled)
				.align(Alignment.BottomEnd)
				.zIndex(2f)
		) {
			FloatingActionButton(
				onClick = {
					dashboardViewModel.dispatch(
						DashboardAction.SetFinancialAction(FinancialAction.NEW)
					)
					
					showFinancialSheet()
				}
			) {
				Icon(
					imageVector = Icons.Rounded.Add,
					contentDescription = null,
				)
			}
		}
		
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.systemBarsPadding()
		) {
			TopAppBar {
				IconButton(
					onClick = {
						showNavRail = !showNavRail
					},
					modifier = Modifier
						.padding(menuIconPadding)
						.align(Alignment.CenterStart)
				) {
					Icon(
						imageVector = Icons.Rounded.Menu,
						contentDescription = null
					)
				}
				
				Text(
					text = stringResource(id = R.string.dashboard),
					style = Typography.titleLarge.copy(
						fontWeight = FontWeight.Bold,
						fontSize = Typography.titleLarge.fontSize.spScaled
					)
				)
				
				IconButton(
					onClick = onSettingClicked,
					modifier = Modifier
						.align(Alignment.CenterEnd)
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_setting),
						contentDescription = null
					)
				}
			}
			
			Row(
				modifier = Modifier
					.fillMaxWidth()
			) {
				DashboardNavigationRail(
					visible = showNavRail,
					selectedItem = selectedNavRailItem,
					items = navigationRailItem,
					onItemSelected = { pair ->
						selectedNavRailItem = pair
						
						when (pair.first) {
							navigationRailItem[0].first -> {
								popupDashboardNavigation(
									toRoute = DujerDestination.Dashboard.Home.route,
									dashboardNavController = dashboardNavController
								)
							}
							navigationRailItem[1].first -> {
								popupDashboardNavigation(
									toRoute = DujerDestination.Dashboard.Chart.route,
									dashboardNavController = dashboardNavController
								)
							}
							navigationRailItem[2].first -> {
								popupDashboardNavigation(
									toRoute = DujerDestination.Dashboard.Export.route,
									dashboardNavController = dashboardNavController
								)
							}
						}
					}
				)
				
				AnimatedNavHost(
					navController = dashboardNavController,
					startDestination = DujerDestination.Dashboard.Home.route,
					enterTransition = {
						slideIntoContainer(
							towards = AnimatedContentScope.SlideDirection.Down
						)
					},
					popEnterTransition = {
						slideIntoContainer(
							towards = AnimatedContentScope.SlideDirection.Up
						)
					}
				) {
					composable(DujerDestination.Dashboard.Home.route) {
						DashboardHomeScreen(
							wallets = wallets,
							incomeTransaction = incomeTransaction,
							incomeLineDataset = incomeLineDataset,
							expenseTransaction = expenseTransaction,
							expenseLineDataset = expenseLineDataset,
							navController = navController,
							onFinancialCardClicked = { financial ->
								dashboardViewModel.dispatch(
									DashboardAction.SetFinancialAction(FinancialAction.EDIT)
								)
								
								dashboardViewModel.dispatch(
									DashboardAction.SetFinancialID(financial.id)
								)
								
								showFinancialSheet()
							},
							onFinancialCardDismissToEnd = { financial ->
								dujerViewModel.dispatch(
									DujerAction.DeleteFinancial(financial.toArray())
								)
							},
							onFinancialCardCanDelete = {
								vibrate()
							},
							onWalletSheetOpened = { isOpened ->
								showFABNewTransaction = !isOpened
							},
							onAddWallet = { wallet ->
								dashboardViewModel.dispatch(
									DashboardAction.NewWallet(wallet)
								)
							}
						)
					}
					
					composable(DujerDestination.Dashboard.Chart.route) {
						ChartScreen(
							dashboardNavController = dashboardNavController,
							onFinancialCardClicked = { financial ->
								dashboardViewModel.dispatch(
									DashboardAction.SetFinancialAction(FinancialAction.EDIT)
								)
								
								dashboardViewModel.dispatch(
									DashboardAction.SetFinancialID(financial.id)
								)
								
								showFinancialSheet()
							},
							onFinancialCardDismissToEnd = { financial ->
								dujerViewModel.dispatch(
									DujerAction.DeleteFinancial(financial.toArray())
								)
							},
							onFinancialCardCanDelete = {
								vibrate()
							}
						)
					}
					
					composable(DujerDestination.Dashboard.Export.route) {
					
					}
				}
				
			}
		}
	}
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun DashboardHomeScreen(
	wallets: List<Wallet>,
	incomeTransaction: List<Financial>,
	incomeLineDataset: LineDataSet,
	expenseTransaction: List<Financial>,
	expenseLineDataset: LineDataSet,
	navController: NavController,
	onFinancialCardDismissToEnd: (Financial) -> Unit,
	onFinancialCardCanDelete: (Financial) -> Unit,
	onFinancialCardClicked: (Financial) -> Unit,
	onWalletSheetOpened: (Boolean) -> Unit,
	onAddWallet: (Wallet) -> Unit
) {
	
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val scope = rememberCoroutineScope()
	
	val addWalletSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val mixedTransaction = remember(incomeTransaction, expenseTransaction) {
		incomeTransaction.merge(expenseTransaction).sortedBy { it.dateCreated }
	}
	
	val totalAmountIncomeList = remember(incomeTransaction) { incomeTransaction.sumOf { it.amount } }
	val totalAmountExpenseList = remember(expenseTransaction) { expenseTransaction.sumOf { it.amount } }
	
	val walletNameFocusRequester by remember { mutableStateOf(FocusRequester()) }
	
	val hideAddWalletSheet = {
		scope.launch { addWalletSheetState.hide() }
		Unit
	}
	
	val showAddWalletSheet = {
		scope.launch { addWalletSheetState.show() }
		Unit
	}
	
	LaunchedEffect(addWalletSheetState.isVisible) {
		onWalletSheetOpened(addWalletSheetState.isVisible)
		if (!addWalletSheetState.isVisible) {
			keyboardController?.hide()
			focusManager.clearFocus(force = true)
		}
		if (addWalletSheetState.isVisible) {
			delay(200)
			walletNameFocusRequester.requestFocus()
		}
	}
	
	BackHandler(enabled = addWalletSheetState.isVisible) {
		hideAddWalletSheet()
	}
	
	ModalBottomSheetLayout(
		sheetElevation = 8.dpScaled,
		scrimColor = Color.Transparent,
		sheetState = addWalletSheetState,
		sheetShape = RoundedCornerShape(
			topStart = shapes.medium.topStart,
			topEnd = shapes.medium.topEnd,
			bottomEnd = CornerSize(0.dpScaled),
			bottomStart = CornerSize(0.dpScaled)
		),
		sheetContent = {
			AddWalletScreen(
				isScreenVisible = addWalletSheetState.isVisible,
				walletNameFocusRequester = walletNameFocusRequester,
				onCancel = hideAddWalletSheet,
				onSave = { wallet ->
					onAddWallet(wallet)
					hideAddWalletSheet()
				}
			)
		}
	) {
		LazyColumn {
			
			item {
				Column(
					modifier = Modifier
						.padding(horizontal = 12.dpScaled)
				) {
					
					BalanceCard(
						wallets = wallets,
						onAddWallet = showAddWalletSheet,
						onWalletClicked = { wallet ->
							navController.navigate(
								DujerDestination.Wallet.createRoute(wallet.id)
							) {
								launchSingleTop = true
								restoreState = true
							}
						}
					)
					
					BudgetCard(
						totalExpense = totalAmountExpenseList,
						totalIncome = totalAmountIncomeList,
						modifier = Modifier
							.padding(top = 16.dpScaled)
					)
					
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.padding(
								vertical = 16.dpScaled
							)
					) {
						IncomeCard(
							income = incomeTransaction.getBy { it.amount }.sum(),
							onClick = {
								navController.navigate(
									DujerDestination.IncomeExpense.createRoute(FinancialType.INCOME)
								) {
									launchSingleTop = true
								}
							},
							modifier = Modifier
								.padding(end = 4.dpScaled)
								.weight(1f)
						)
						
						ExpenseCard(
							expense = expenseTransaction.getBy { it.amount }.sum(),
							onClick = {
								navController.navigate(
									DujerDestination.IncomeExpense.createRoute(FinancialType.EXPENSE)
								) {
									launchSingleTop = true
								}
							},
							modifier = Modifier
								.padding(start = 4.dpScaled)
								.weight(1f)
						)
					}
					
					FinancialLineChart(
						incomeLineDataset = incomeLineDataset,
						expenseLineDataset = expenseLineDataset
					)
				}
			}
			
			items(
				items = mixedTransaction,
				key = { item: Financial -> item.hashCode() }
			) { financial ->
				SwipeableFinancialCard(
					financial = financial,
					onDismissToEnd = { onFinancialCardDismissToEnd(financial) },
					onCanDelete = { onFinancialCardCanDelete(financial) },
					onClick = { onFinancialCardClicked(financial) },
					modifier = Modifier
						.padding(horizontal = 12.dpScaled)
						.testTag("SwipeableFinancialCard")
				)
			}
			
			item {
				// fab size: 56 dp, fab bottom padding: 24 dp, card to fab padding: 16 dp = 96 dp
				Spacer(
					modifier = Modifier
						.fillMaxWidth()
						.height(96.dpScaled)
				)
			}
		}
	}
}

internal fun popupDashboardNavigation(
	toRoute: String,
	dashboardNavController: NavController
) {
	dashboardNavController.navigate(toRoute) {
		dashboardNavController.graph.startDestinationRoute?.let { route ->
			popUpTo(route) {
				saveState = true
			}
		}
		
		restoreState = true
		launchSingleTop = true
	}
}
