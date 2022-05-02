package com.anafthdev.dujer.ui.dashboard

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.merge
import com.anafthdev.dujer.foundation.extension.toArray
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.app.DujerAction
import com.anafthdev.dujer.ui.app.DujerViewModel
import com.anafthdev.dujer.ui.chart.ChartScreen
import com.anafthdev.dujer.ui.dashboard.component.*
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.search.SearchScreen
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.expenseColor
import com.anafthdev.dujer.ui.theme.incomeColor
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.github.mikephil.charting.data.LineDataSet
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val navigationRailItem = listOf(
	"Dashboard" to R.drawable.ic_dashboard,
	"Chart" to R.drawable.ic_chart,
	"Export" to R.drawable.ic_export
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
	navController: NavController,
	dujerViewModel: DujerViewModel
) {
	
	val context = LocalContext.current
	val config = LocalConfiguration.current
	
	val dashboardViewModel = hiltViewModel<DashboardViewModel>()
	
	val scope = rememberCoroutineScope { Dispatchers.Main }
	val financialScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val state by dashboardViewModel.state.collectAsState()
	val incomeLineDataSetEntry = state.incomeLineDataSetEntry
	val expenseLineDataSetEntry = state.expenseLineDataSetEntry
	val financial = state.financial
	val financialAction = state.financialAction
	
	val incomeLineDataset by rememberUpdatedState(
		newValue = LineDataSet(
			incomeLineDataSetEntry,
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
	
	val expenseLineDataset by rememberUpdatedState(
		newValue = LineDataSet(
			expenseLineDataSetEntry,
			context.getString(R.string.income)
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
					dashboardState = state,
					incomeLineDataset = incomeLineDataset,
					expenseLineDataset = expenseLineDataset,
					financialScreenSheetState = financialScreenSheetState,
					dujerViewModel = dujerViewModel,
					dashboardViewModel = dashboardViewModel,
					navController = navController,
					onSettingClicked = {
						navController.navigate(DujerDestination.Setting.route) {
							popUpTo(popUpToId) {
								saveState = true
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
	dashboardState: DashboardState,
	incomeLineDataset: LineDataSet,
	expenseLineDataset: LineDataSet,
	financialScreenSheetState: ModalBottomSheetState,
	dujerViewModel: DujerViewModel,
	dashboardViewModel: DashboardViewModel,
	navController: NavController,
	onSettingClicked: () -> Unit
) {
	// TODO: animatedNavController reset pas ke setting screen terus back
	
	val context = LocalContext.current
	
	val userBalance = dashboardState.userBalance
	val incomeFinancialList = dashboardState.incomeFinancialList
	val expenseFinancialList = dashboardState.expenseFinancialList
	
	val scope = rememberCoroutineScope { Dispatchers.Main }
	val dashboardNavController = rememberAnimatedNavController()
	
	val currentRoute = dashboardNavController.currentDestination?.route
	
	val openFinancialSheet = {
		scope.launch { financialScreenSheetState.show() }
		Unit
	}
	
	val vibrate = {
		dujerViewModel.dispatch(
			DujerAction.Vibrate(100)
		)
	}
	
	var showNavRail by remember { mutableStateOf(false) }
	var selectedNavRailItem by remember { mutableStateOf(navigationRailItem[0]) }
	
	val menuIconPadding by animateDpAsState(
		targetValue = if (showNavRail) 16.dpScaled else 0.dpScaled,
		animationSpec = tween(400)
	)
	
	BackHandler {
		when {
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
		FloatingActionButton(
			onClick = {
				dashboardViewModel.dispatch(
					DashboardAction.SetFinancialAction(FinancialAction.NEW)
				)
				
				openFinancialSheet()
			},
			modifier = Modifier
				.padding(32.dpScaled)
				.align(Alignment.BottomEnd)
				.zIndex(2f)
		) {
			Icon(
				imageVector = Icons.Rounded.Add,
				contentDescription = null,
			)
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
							userBalance = userBalance,
							incomeFinancialList = incomeFinancialList,
							incomeLineDataset = incomeLineDataset,
							expenseFinancialList = expenseFinancialList,
							expenseLineDataset = expenseLineDataset,
							navController = navController,
							onFinancialCardClicked = { financial ->
								dashboardViewModel.dispatch(
									DashboardAction.SetFinancialAction(FinancialAction.EDIT)
								)
								
								dashboardViewModel.dispatch(
									DashboardAction.SetFinancialID(financial.id)
								)
								
								openFinancialSheet()
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
								
								openFinancialSheet()
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

@Composable
private fun DashboardHomeScreen(
	userBalance: Double,
	incomeFinancialList: List<Financial>,
	incomeLineDataset: LineDataSet,
	expenseFinancialList: List<Financial>,
	expenseLineDataset: LineDataSet,
	navController: NavController,
	onFinancialCardDismissToEnd: (Financial) -> Unit,
	onFinancialCardCanDelete: (Financial) -> Unit,
	onFinancialCardClicked: (Financial) -> Unit
) {
	
	val mixedFinancialList = incomeFinancialList.merge(expenseFinancialList).sortedBy { it.dateCreated }
	
	LazyColumn {
		
		item {
			Column(
				modifier = Modifier
					.padding(horizontal = 12.dpScaled)
			) {
				BalanceCard(
					balance = userBalance
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
						income = incomeFinancialList.getBy { it.amount }.sum(),
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
						expense = expenseFinancialList.getBy { it.amount }.sum(),
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
			items = mixedFinancialList,
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
