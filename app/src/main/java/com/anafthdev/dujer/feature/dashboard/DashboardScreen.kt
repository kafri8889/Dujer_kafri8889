package com.anafthdev.dujer.feature.dashboard

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.DujerDestination
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.LocalDujerState
import com.anafthdev.dujer.feature.chart.ChartScreen
import com.anafthdev.dujer.feature.dashboard.component.BalanceCard
import com.anafthdev.dujer.feature.dashboard.component.ExpenseCard
import com.anafthdev.dujer.feature.dashboard.component.HighestExpenseCard
import com.anafthdev.dujer.feature.dashboard.component.IncomeCard
import com.anafthdev.dujer.feature.financial.data.FinancialAction
import com.anafthdev.dujer.feature.search.SearchScreen
import com.anafthdev.dujer.feature.theme.Typography
import com.anafthdev.dujer.feature.theme.expense_color
import com.anafthdev.dujer.feature.theme.income_color
import com.anafthdev.dujer.foundation.ui.LocalUiColor
import com.anafthdev.dujer.foundation.uicomponent.BudgetCard
import com.anafthdev.dujer.foundation.uicomponent.FilterSortFinancialPopup
import com.anafthdev.dujer.foundation.uicomponent.TopAppBar
import com.anafthdev.dujer.foundation.uicomponent.swipeableFinancialCard
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.github.mikephil.charting.data.LineDataSet
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun DashboardScreen(
	navController: NavController,
	viewModel: DashboardViewModel,
	bottomSheetNavigator: BottomSheetNavigator,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val state by viewModel.state.collectAsState()
	
	SearchScreen(
		navController = navController,
		canBack = !bottomSheetNavigator.navigatorSheetState.isVisible,
		content = {
			DashboardContent(
				state = state,
				viewModel = viewModel,
				navController = navController,
				onDeleteTransaction = onDeleteTransaction,
				onSettingClicked = {
					navController.navigate(DujerDestination.Setting.Home.route) {
						launchSingleTop = true
					}
				}
			)
		},
		onFinancialClicked = { clickedFinancial ->
			navController.navigate(
				DujerDestination.BottomSheet.Financial.Home.createRoute(
					financialAction = FinancialAction.EDIT,
					financialID = clickedFinancial.id
				)
			) {
				launchSingleTop = true
			}
		}
	)
	
}

@OptIn(
	ExperimentalAnimationApi::class, ExperimentalPagerApi::class
)
@Composable
private fun DashboardContent(
	state: DashboardState,
	viewModel: DashboardViewModel,
	navController: NavController,
	onSettingClicked: () -> Unit,
	onDeleteTransaction: (Financial) -> Unit
) {
	
	val context = LocalContext.current
	
	val sortType = state.sortType
	val groupType = state.groupType
	val filterDate = state.filterDate
	val selectedMonth = state.selectedMonth
	
	val scope = rememberCoroutineScope()
	
	val dashboardPagerState = rememberPagerState()
	val homeLazyListState = rememberLazyListState()
	
	var showFABNewTransaction by rememberSaveable { mutableStateOf(true) }
	var isFilterSortFinancialPopupShowed by rememberSaveable { mutableStateOf(false) }
	
	LaunchedEffect(dashboardPagerState.currentPage) {
		homeLazyListState.animateScrollToItem(0)
	}
	
	BackHandler {
		when {
			dashboardPagerState.currentPage != 0 -> {
				scope.launch { dashboardPagerState.animateScrollToPage(0) }
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
				groupType = groupType,
				filterDate = filterDate,
				monthsSelected = selectedMonth,
				onApply = { mSelectedMonth, mSortBy, mGroupType, date ->
					viewModel.dispatch(
						DashboardAction.SetSortType(mSortBy)
					)
					
					viewModel.dispatch(
						DashboardAction.SetSelectedMonth(mSelectedMonth)
					)
					
					viewModel.dispatch(
						DashboardAction.SetGroupType(mGroupType)
					)
					
					if (date != null) {
						viewModel.dispatch(
							DashboardAction.SetFilterDate(date)
						)
					}
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
					navController.navigate(
						DujerDestination.BottomSheet.Financial.Home.createRoute(
							financialAction = FinancialAction.NEW,
							financialID = Financial.default.id
						)
					) {
						launchSingleTop = true
					}
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
					AnimatedVisibility(
						visible = dashboardPagerState.currentPage == 0,
						enter = scaleIn(
							animationSpec = tween(800)
						),
						exit = scaleOut(
							animationSpec = tween(800)
						)
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
			
			HorizontalPager(
				count = 2,
				state = dashboardPagerState,
				modifier = Modifier
					.fillMaxWidth()
			) { page ->
				when (page) {
					0 -> DashboardHomeScreen(
						state = state,
						homeLazyListState = homeLazyListState,
						navController = navController,
						onFinancialCardClicked = { financial ->
							navController.navigate(
								DujerDestination.BottomSheet.Financial.Home.createRoute(
									financialAction = FinancialAction.EDIT,
									financialID = financial.id
								)
							) {
								launchSingleTop = true
							}
						},
						onFinancialCardDismissToEnd = onDeleteTransaction,
						onWalletSheetOpened = { isOpened ->
							showFABNewTransaction = !isOpened
						},
						onAddWallet = { wallet ->
							viewModel.dispatch(
								DashboardAction.NewWallet(wallet)
							)
						}
					)
					1 -> ChartScreen(
						onFinancialCardDismissToEnd = onDeleteTransaction,
						onFinancialCardClicked = { financial ->
							navController.navigate(
								DujerDestination.BottomSheet.Financial.Home.createRoute(
									financialAction = FinancialAction.EDIT,
									financialID = financial.id
								)
							) {
								launchSingleTop = true
							}
						}
					)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun DashboardHomeScreen(
	state: DashboardState,
	homeLazyListState: LazyListState,
	navController: NavController,
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
	val incomeEntry = state.incomeEntry
	val expenseEntry = state.expenseEntry
	
	val scope = rememberCoroutineScope()
	
	val allIncomeTransaction = dujerState.allIncomeTransaction
	val allExpenseTransaction = dujerState.allExpenseTransaction

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
	
	val incomeLineDataset by rememberUpdatedState(
		newValue = LineDataSet(
			incomeEntry,
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
	
	val expenseLineDataset by rememberUpdatedState(
		newValue = LineDataSet(
			expenseEntry,
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
	
	LazyColumn(
		state = homeLazyListState
	) {
		
		item {
			Column(
				modifier = Modifier
					.padding(horizontal = 12.dpScaled)
			) {
				
				BalanceCard(
					wallets = wallets,
					onAddWallet = {
						navController.navigate(DujerDestination.BottomSheet.AddWallet.Home.route)
					},
					onWalletClicked = { wallet ->
						navController.navigate(
							DujerDestination.Wallet.Home.createRoute(wallet.id)
						)
					}
				)
				
				BudgetCard(
					totalExpense = totalAmountBudgetExpenses,
					totalBudget = totalAmountBudget,
					onClick = {
						navController.navigate(DujerDestination.BudgetList.Home.route)
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
									DujerDestination.CategoryTransaction.Home.createRoute(
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
						income = allIncomeTransaction.sumOf { it.amount },
						onClick = {
							navController.navigate(
								DujerDestination.IncomeExpense.Home.createRoute(FinancialType.INCOME)
							) {
								launchSingleTop = true
							}
						},
						modifier = Modifier
							.padding(end = 4.dpScaled)
							.weight(1f)
					)
					
					ExpenseCard(
						expense = allExpenseTransaction.sumOf { it.amount },
						onClick = {
							navController.navigate(
								DujerDestination.IncomeExpense.Home.createRoute(FinancialType.EXPENSE)
							) {
								launchSingleTop = true
							}
						},
						modifier = Modifier
							.padding(start = 4.dpScaled)
							.weight(1f)
					)
				}

//					FinancialLineChart(
//						incomeLineDataset = incomeLineDataset,
//						expenseLineDataset = expenseLineDataset
//					)
			}
		}
		
		swipeableFinancialCard(
			data = transactions,
			onFinancialCardDismissToEnd = { onFinancialCardDismissToEnd(it) },
			onFinancialCardClicked = { onFinancialCardClicked(it) },
			onNavigateCategoryClicked = { category ->
				navController.navigate(
					DujerDestination.CategoryTransaction.Home.createRoute(category.id)
				)
			}
		)
		
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
