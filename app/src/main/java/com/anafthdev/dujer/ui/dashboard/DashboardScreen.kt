package com.anafthdev.dujer.ui.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
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
import com.anafthdev.dujer.foundation.extension.combine
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.Currency
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
	val userBalance = state.userBalance
	val currentCurrency = state.currentCurrency
	val incomeFinancialList = state.incomeFinancialList
	val expenseFinancialList = state.expenseFinancialList
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
					userBalance = userBalance,
					currentCurrency = currentCurrency,
					incomeFinancialList = incomeFinancialList,
					expenseFinancialList = expenseFinancialList,
					incomeLineDataset = incomeLineDataset,
					expenseLineDataset = expenseLineDataset,
					scope = scope,
					financialScreenSheetState = financialScreenSheetState,
					dujerViewModel = dujerViewModel,
					dashboardViewModel = dashboardViewModel,
					navController = navController
				)
			},
			onFinancialClicked = { clickedFinancial ->
				dashboardViewModel.setFinancialAction(FinancialAction.EDIT)
				dashboardViewModel.setFinancialID(clickedFinancial.id)
				scope.launch {
					financialScreenSheetState.show()
				}
			}
		)
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DashboardContent(
	userBalance: Double,
	currentCurrency: Currency,
	incomeFinancialList: List<Financial>,
	expenseFinancialList: List<Financial>,
	incomeLineDataset: LineDataSet,
	expenseLineDataset: LineDataSet,
	scope: CoroutineScope,
	financialScreenSheetState: ModalBottomSheetState,
	dujerViewModel: DujerViewModel,
	dashboardViewModel: DashboardViewModel,
	navController: NavController
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		FloatingActionButton(
			onClick = {
				dashboardViewModel.setFinancialAction(FinancialAction.NEW)
				scope.launch {
					financialScreenSheetState.show()
				}
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
		
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
				.systemBarsPadding()
		) {
			item {
				Column(
					modifier = Modifier
						.fillMaxSize()
				) {
					TopAppBar {
						Text(
							text = stringResource(id = R.string.dashboard),
							style = Typography.titleLarge.copy(
								fontWeight = FontWeight.Bold,
								fontSize = Typography.titleLarge.fontSize.spScaled
							)
						)
						
						IconButton(
							onClick = {
								navController.navigate(DujerDestination.Setting.route) {
									launchSingleTop = true
								}
							},
							modifier = Modifier
								.align(Alignment.CenterEnd)
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_setting),
								contentDescription = null
							)
						}
					}
					
					Column(
						modifier = Modifier
							.padding(horizontal = 16.dpScaled)
					) {
						BalanceCard(
							balance = userBalance,
							currency = currentCurrency
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
								currency = currentCurrency,
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
								currency = currentCurrency,
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
					}
				}
				
				FinancialLineChart(
					incomeLineDataset = incomeLineDataset,
					expenseLineDataset = expenseLineDataset
				)
			}
			
			items(
				items = incomeFinancialList.combine(expenseFinancialList).sortedBy { it.dateCreated },
				key = { item: Financial -> item.id }
			) { financial ->
				SwipeableFinancialCard(
					financial = financial,
					onDismissToEnd = {
						dashboardViewModel.delete(financial)
					},
					onCanDelete = {
						dujerViewModel.vibrate(100)
					},
					onClick = {
						dashboardViewModel.setFinancialAction(FinancialAction.EDIT)
						dashboardViewModel.setFinancialID(financial.id)
						scope.launch {
							financialScreenSheetState.show()
						}
					},
					modifier = Modifier
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
