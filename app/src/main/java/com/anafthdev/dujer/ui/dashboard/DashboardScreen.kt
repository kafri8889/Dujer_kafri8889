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
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Menu
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
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.app.LocalDujerState
import com.anafthdev.dujer.ui.chart.ChartScreen
import com.anafthdev.dujer.ui.dashboard.component.*
import com.anafthdev.dujer.ui.dashboard.subscreen.AddWalletScreen
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.search.SearchScreen
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.expense_color
import com.anafthdev.dujer.ui.theme.income_color
import com.anafthdev.dujer.ui.theme.shapes
import com.anafthdev.dujer.uicomponent.BudgetCard
import com.anafthdev.dujer.uicomponent.FilterSortFinancialPopup
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(
	navController: NavController,
	onTransactionCanDelete: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit
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
					state = state,
					financialScreenSheetState = financialScreenSheetState,
					viewModel = dashboardViewModel,
					navController = navController,
					onTransactionCanDelete = onTransactionCanDelete,
					onDeleteTransaction = onDeleteTransaction,
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

@OptIn(ExperimentalMaterialApi::class,
	ExperimentalAnimationApi::class
)
@Composable
private fun DashboardContent(
	state: DashboardState,
	financialScreenSheetState: ModalBottomSheetState,
	viewModel: DashboardViewModel,
	navController: NavController,
	onSettingClicked: () -> Unit,
	onTransactionCanDelete: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val context = LocalContext.current
	
	val scope = rememberCoroutineScope()
	val dashboardNavController = rememberAnimatedNavController()
	
	val currentRoute = dashboardNavController.currentDestination?.route
	
	val sortType = state.sortType
	val selectedMonth = state.selectedMonth
	
	var showNavRail by rememberSaveable { mutableStateOf(false) }
	var showFABNewTransaction by rememberSaveable { mutableStateOf(true) }
	var selectedNavRailItem by rememberSaveable { mutableStateOf(navigationRailItem[0]) }
	var isFilterSortFinancialPopupShowed by rememberSaveable { mutableStateOf(false) }
	
	val menuIconPadding by animateDpAsState(
		targetValue = if (showNavRail) 16.dpScaled else 0.dpScaled,
		animationSpec = tween(400)
	)
	
	val showFinancialSheet = {
		scope.launch { financialScreenSheetState.show() }
		Unit
	}
	
	val hideFinancialSheet = {
		scope.launch { financialScreenSheetState.hide() }
		Unit
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
			visible = isFilterSortFinancialPopupShowed,
			enter = fadeIn(
				animationSpec = tween(400)
			),
			exit = fadeOut(
				animationSpec = tween(400)
			),
			modifier = Modifier
				.fillMaxSize()
				.zIndex(2f)
		) {
			FilterSortFinancialPopup(
				isVisible = isFilterSortFinancialPopupShowed,
				sortType = sortType,
				monthsSelected = selectedMonth,
				onApply = { mSelectedMonth, mSortBy ->
					viewModel.dispatch(
						DashboardAction.SetSortType(mSortBy)
					)
					
					viewModel.dispatch(
						DashboardAction.SetSelectedMonth(mSelectedMonth)
					)
				},
				onClose = {
					isFilterSortFinancialPopupShowed = false
				},
				onClickOutside = {
					isFilterSortFinancialPopupShowed = false
				},
				modifier = Modifier
					.systemBarsPadding()
					.padding(vertical = 24.dpScaled)
			)
		}
		
		AnimatedVisibility(
			visible = showFABNewTransaction and !isFilterSortFinancialPopupShowed,
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
					viewModel.dispatch(
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
						color = LocalUiColor.current.headlineText,
						fontWeight = FontWeight.Bold,
						fontSize = Typography.titleLarge.fontSize.spScaled
					)
				)
				
				Row(
					modifier = Modifier
						.padding(end = 8.dpScaled)
						.align(Alignment.CenterEnd)
				) {
					IconButton(
						onClick = {
							isFilterSortFinancialPopupShowed = true
						},
					) {
						Icon(
							imageVector = Icons.Rounded.FilterList,
							contentDescription = null
						)
					}
					
					IconButton(
						onClick = onSettingClicked,
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_setting),
							contentDescription = null
						)
					}
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
							state = state,
							viewModel = viewModel,
							navController = navController,
							onFinancialCardClicked = { financial ->
								viewModel.dispatch(
									DashboardAction.SetFinancialAction(FinancialAction.EDIT)
								)
								
								viewModel.dispatch(
									DashboardAction.SetFinancialID(financial.id)
								)
								
								showFinancialSheet()
							},
							onFinancialCardDismissToEnd = onDeleteTransaction,
							onFinancialCardCanDelete = onTransactionCanDelete,
							onWalletSheetOpened = { isOpened ->
								showFABNewTransaction = !isOpened
							},
							onAddWallet = { wallet ->
								viewModel.dispatch(
									DashboardAction.NewWallet(wallet)
								)
							}
						)
					}
					
					composable(DujerDestination.Dashboard.Chart.route) {
						ChartScreen(
							onFinancialCardDismissToEnd = onDeleteTransaction,
							onFinancialCardCanDelete = onTransactionCanDelete,
							onFinancialCardClicked = { financial ->
								viewModel.dispatch(
									DashboardAction.SetFinancialAction(FinancialAction.EDIT)
								)
								
								viewModel.dispatch(
									DashboardAction.SetFinancialID(financial.id)
								)
								
								showFinancialSheet()
							},
							onBudgetCardClicked = {
								navController.navigate(DujerDestination.BudgetList.route)
							},
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
	state: DashboardState,
	viewModel: DashboardViewModel,
	navController: NavController,
	onFinancialCardCanDelete: () -> Unit,
	onFinancialCardDismissToEnd: (Financial) -> Unit,
	onFinancialCardClicked: (Financial) -> Unit,
	onWalletSheetOpened: (Boolean) -> Unit,
	onAddWallet: (Wallet) -> Unit
) {
	
	val context = LocalContext.current
	val dujerState = LocalDujerState.current
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	
	val transactions = state.transactions
	
	val wallets = dujerState.allWallet
	val allBudget = dujerState.allBudget
	
	val highestExpenseCategory = state.highestExpenseCategory
	val highestExpenseCategoryAmount = state.highestExpenseCategoryAmount
	
	val scope = rememberCoroutineScope()
	
	val addWalletSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val allIncomeTransaction = dujerState.allIncomeTransaction
	val allExpenseTransaction = dujerState.allExpenseTransaction
	
	val incomeTransaction = remember(transactions) {
		transactions.filter { it.type == FinancialType.INCOME }
	}
	val expenseTransaction = remember(transactions) {
		transactions.filter { it.type == FinancialType.EXPENSE }
	}
	
	val totalAmountBudget = remember(allBudget) { allBudget.sumOf { it.max } }
	val totalAmountBudgetExpenses = remember(allBudget, allExpenseTransaction) {
		val amount = arrayListOf<Double>()
		allBudget.forEach { budget ->
			amount.add(
				allExpenseTransaction.filter { it.category.id == budget.category.id }
					.sumOf { it.amount }
			)
		}
		
		amount.sum()
	}
	
	val walletNameFocusRequester by remember { mutableStateOf(FocusRequester()) }
	
	val incomeLineDataset by remember {
		mutableStateOf(
			LineDataSet(
				emptyList(),
				context.getString(R.string.income)
			).apply {
				lineWidth = 2.5f
				cubicIntensity = .2f
				mode = LineDataSet.Mode.CUBIC_BEZIER
				color = income_color.toArgb()
				setDrawValues(false)
				setDrawFilled(false)
				setDrawCircles(false)
				setCircleColor(income_color.toArgb())
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
				color = expense_color.toArgb()
				setDrawValues(false)
				setDrawFilled(false)
				setDrawCircles(false)
				setCircleColor(expense_color.toArgb())
				setDrawHorizontalHighlightIndicator(false)
			}
		)
	}
	
	val hideAddWalletSheet = {
		scope.launch { addWalletSheetState.hide() }
		Unit
	}
	
	val showAddWalletSheet = {
		scope.launch { addWalletSheetState.show() }
		Unit
	}
	
	LaunchedEffect(incomeTransaction, expenseTransaction) {
		val (incomeEntry, expenseEntry) = viewModel.getLineDataSetEntry(
			incomeList = incomeTransaction,
			expenseList = expenseTransaction
		)
		
		incomeLineDataset.values = incomeEntry
		expenseLineDataset.values = expenseEntry
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
							)
						}
					)
					
					BudgetCard(
						totalExpense = totalAmountBudgetExpenses,
						totalBudget = totalAmountBudget,
						onClick = {
							navController.navigate(DujerDestination.BudgetList.route)
						},
						modifier = Modifier
							.padding(top = 16.dpScaled)
					)
					
					AnimatedVisibility(
						visible = (highestExpenseCategory.id != Category.default.id).and(
							highestExpenseCategoryAmount != 0.0
						)
					) {
						HighestExpenseCard(
							category = highestExpenseCategory,
							totalExpense = highestExpenseCategoryAmount,
							onClick = {
								if (highestExpenseCategory.id != Category.default.id) {
									navController.navigate(
										DujerDestination.CategoryTransaction.createRoute(
											highestExpenseCategory.id
										)
									) {
										launchSingleTop = true
									}
								}
							},
							modifier = Modifier
								.padding(top = 16.dpScaled)
						)
					}
					
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.padding(
								vertical = 16.dpScaled
							)
					) {
						IncomeCard(
							income = allIncomeTransaction.getBy { it.amount }.sum(),
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
							expense = allExpenseTransaction.getBy { it.amount }.sum(),
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
				items = transactions,
				key = { item: Financial -> item.hashCode() }
			) { financial ->
				SwipeableFinancialCard(
					financial = financial,
					onCanDelete = onFinancialCardCanDelete,
					onDismissToEnd = { onFinancialCardDismissToEnd(financial) },
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
