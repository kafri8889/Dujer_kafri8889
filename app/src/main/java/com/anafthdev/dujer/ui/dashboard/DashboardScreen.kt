package com.anafthdev.dujer.ui.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalDensity
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
import com.anafthdev.dujer.foundation.extension.combine
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.lastIndexOf
import com.anafthdev.dujer.foundation.extension.toArray
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.app.DujerAction
import com.anafthdev.dujer.ui.app.DujerViewModel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val navigationRailItem = listOf(
	"Dashboard" to R.drawable.ic_dashboard,
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

//	val incomeLineDataSetEntry by rememberUpdatedState(
//		newValue = arrayListOf<Entry>().apply {
//			add(Entry(0f, 2200f))
//			add(Entry(1f, 1000f))
//			add(Entry(2f, 3500f))
//			add(Entry(3f, 0f))
//			add(Entry(4f, 9500f))
//			add(Entry(5f, 4000f))
//			add(Entry(6f, 10000f))
//			add(Entry(7f, 12000f))
//			add(Entry(8f, 6400f))
//			add(Entry(9f, 15200f))
//			add(Entry(10f, 7000f))
//			add(Entry(11f, 3500f))
//		}
//	)
	
//	val expenseLineDataSetEntry by rememberUpdatedState(
//		newValue = arrayListOf<Entry>().apply {
//			add(Entry(0f, 1200f))
//			add(Entry(1f, 2800f))
//			add(Entry(2f, 7500f))
//			add(Entry(3f, 6256f))
//			add(Entry(4f, 2400f))
//			add(Entry(5f, 0f))
//			add(Entry(6f, 12200f))
//			add(Entry(7f, 1300f))
//			add(Entry(8f, 5000f))
//			add(Entry(9f, 11200f))
//			add(Entry(10f, 4900f))
//			add(Entry(11f, 6300f))
//		}
//	)
	
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
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
	
	val density = LocalDensity.current
	
	val userBalance = dashboardState.userBalance
	val incomeFinancialList = dashboardState.incomeFinancialList
	val expenseFinancialList = dashboardState.expenseFinancialList
	
	val mixedFinancialList = incomeFinancialList
		.combine(expenseFinancialList)
		.sortedBy { it.dateCreated }
	
	val scope = rememberCoroutineScope { Dispatchers.Main }
	
	val openFinancialSheet = {
		scope.launch { financialScreenSheetState.show() }
		Unit
	}
	
	var showNavRail by remember { mutableStateOf(false) }
	var selectedNavRailItem by remember { mutableStateOf(navigationRailItem[0]) }
	
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
				.verticalScroll(rememberScrollState())
		) {
			TopAppBar {
				IconButton(
					onClick = {
						showNavRail = !showNavRail
					},
					modifier = Modifier
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
					onDismissRequest = {
						showNavRail = false
					},
					onItemSelected = { pair ->
						selectedNavRailItem = pair
					}
				)
				
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
					
					mixedFinancialList.forEachIndexed { i, financial ->
						key(financial.hashCode(), i) {
							SwipeableFinancialCard(
								financial = financial,
								onDismissToEnd = {
									dujerViewModel.dispatch(
										DujerAction.DeleteFinancial(
											financial.toArray()
										)
									)
								},
								onCanDelete = {
									dujerViewModel.dispatch(
										DujerAction.Vibrate(100)
									)
								},
								onClick = {
									dashboardViewModel.dispatch(
										DashboardAction.SetFinancialAction(FinancialAction.EDIT)
									)

									dashboardViewModel.dispatch(
										DashboardAction.SetFinancialID(financial.id)
									)

									openFinancialSheet()
								},
								modifier = Modifier
									.testTag("SwipeableFinancialCard")
							)
						}

						if (i.lastIndexOf(mixedFinancialList)) {
							// fab size: 56 dp, fab bottom padding: 24 dp, card to fab padding: 16 dp = 96 dp
							Spacer(
								modifier = Modifier
									.fillMaxWidth()
									.height(96.dpScaled)
							)
						}
					}
					
//					LazyColumn {
//
//						items(
//							items = incomeFinancialList.combine(expenseFinancialList).sortedBy { it.dateCreated },
//							key = { item: Financial -> item.hashCode() }
//						) { financial ->
//							SwipeableFinancialCard(
//								financial = financial,
//								onDismissToEnd = {
//									dujerViewModel.dispatch(
//										DujerAction.DeleteFinancial(
//											financial.toArray()
//										)
//									)
//								},
//								onCanDelete = {
//									dujerViewModel.dispatch(
//										DujerAction.Vibrate(100)
//									)
//								},
//								onClick = {
//									dashboardViewModel.dispatch(
//										DashboardAction.SetFinancialAction(FinancialAction.EDIT)
//									)
//
//									dashboardViewModel.dispatch(
//										DashboardAction.SetFinancialID(financial.id)
//									)
//
//									openFinancialSheet()
//								},
//								modifier = Modifier
//									.testTag("SwipeableFinancialCard")
//							)
//						}
//
//						item {
//							// fab size: 56 dp, fab bottom padding: 24 dp, card to fab padding: 16 dp = 96 dp
//							Spacer(
//								modifier = Modifier
//									.fillMaxWidth()
//									.height(96.dpScaled)
//							)
//						}
//					}
				}
			}
		}
	}
}
