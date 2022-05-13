package com.anafthdev.dujer.ui.chart

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.merge
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.chart.data.MonthBarChartFormatter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.expenseColor
import com.anafthdev.dujer.ui.theme.extra_large_shape
import com.anafthdev.dujer.ui.theme.incomeColor
import com.anafthdev.dujer.uicomponent.BudgetCard
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.YearSelector
import com.anafthdev.dujer.uicomponent.charting.bar.BarChart
import com.anafthdev.dujer.uicomponent.charting.bar.components.XAxisVisibility
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarChartDefault
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarDataSet
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.uicomponent.charting.bar.rememberBarChartState
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChartScreen(
	dashboardNavController: NavController,
	onFinancialCardDismissToEnd: (Financial) -> Unit,
	onFinancialCardCanDelete: (Financial) -> Unit,
	onFinancialCardClicked: (Financial) -> Unit
) {
	
	val config = LocalConfiguration.current
	val density = LocalDensity.current
	
	val barDataSets = listOf(
		BarDataSet(BarData.sample1),
		BarDataSet(BarData.sample2)
	)
	
	val chartViewModel = hiltViewModel<ChartViewModel>()
	
	val state by chartViewModel.state.collectAsState()
	
	val incomeFinancialList = state.incomeFinancialList
	val expenseFinancialList = state.expenseFinancialList
	val incomeBarDataList = state.incomeBarDataList
	val expenseBarDataList = state.expenseBarDataList
	
	var selectedYear by remember { mutableStateOf(System.currentTimeMillis()) }
	var selectedBarDataGroup by remember { mutableStateOf(Calendar.getInstance()[Calendar.MONTH]) }
	val totalAmountIncomeList = remember(incomeFinancialList) { incomeFinancialList.sumOf { it.amount } }
	val totalAmountExpenseList = remember(expenseFinancialList) { expenseFinancialList.sumOf { it.amount } }
	val lazyListValue = remember(
		selectedYear,
		selectedBarDataGroup,
		incomeFinancialList,
		expenseFinancialList
	) {
		incomeFinancialList.merge(
			expenseFinancialList
		).filter {
			chartViewModel.monthFormatter.format(
				it.dateCreated
			) == chartViewModel.monthFormatter.format(chartViewModel.getTimeInMillis(selectedBarDataGroup))
		}
	}
	
	var enterBarChartAnimOffset by remember {
		mutableStateOf(with(density) { config.screenWidthDp.dp.roundToPx() })
	}
	var exitBarChartAnimOffset by remember {
		mutableStateOf(with(density) { -config.screenWidthDp.dp.roundToPx() })
	}
	
	val barChartState = rememberBarChartState(
		lazyListState = rememberLazyListState(),
		initialSelectedBarDataGroup = Calendar.getInstance()[Calendar.MONTH]
	) { barDataGroupID ->
		selectedBarDataGroup = barDataGroupID
	}
	
	LaunchedEffect(selectedYear) {
		delay(400)
		barChartState.lazyListState.animateScrollToItem(selectedBarDataGroup)
	}
	
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.systemBarsPadding()
	) {
		item {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.fillMaxSize()
			) {
				
				YearSelector(
					initialTimeInMillis = System.currentTimeMillis(),
					maxYear = chartViewModel.yearFormatter.format(System.currentTimeMillis()),
					onYearSelected = { year ->
						if (selectedYear < year) {
							enterBarChartAnimOffset = with(density) { config.screenWidthDp.dp.roundToPx() }
							exitBarChartAnimOffset = with(density) { -config.screenWidthDp.dp.roundToPx() }
						} else {
							enterBarChartAnimOffset = with(density) { -config.screenWidthDp.dp.roundToPx() }
							exitBarChartAnimOffset = with(density) { config.screenWidthDp.dp.roundToPx() }
						}
						
						selectedYear = year
						
						chartViewModel.dispatch(
							ChartAction.GetData(
								yearInMillis = selectedYear
							)
						)
					}
				)
				
				AnimatedContent(
					targetState = selectedYear,
					transitionSpec = {
						slideInHorizontally(
							initialOffsetX = { enterBarChartAnimOffset },
							animationSpec = tween(600)
						) with slideOutHorizontally(
							targetOffsetX = { exitBarChartAnimOffset },
							animationSpec = tween(600)
						)
					}
				) {
					BarChart(
						state = barChartState,
						formatter = MonthBarChartFormatter(),
//						barDataSets = barDataSets,
						barDataSets = listOf(
							BarDataSet(incomeBarDataList),
							BarDataSet(expenseBarDataList)
						),
						style = listOf(
							BarChartDefault.barStyle(
								selectedBarColor = incomeColor,
								selectedStartPaddingBarContainer = 24.dpScaled,
								selectedShowXAxisLine = false,
								unSelectedShowXAxisLine = true,
								selectedXAxisLineAnimationSpec = spring(
									stiffness = Spring.StiffnessVeryLow,
									dampingRatio = Spring.DampingRatioHighBouncy
								)
							),
							BarChartDefault.barStyle(
								selectedBarColor = expenseColor,
								selectedEndPaddingBarContainer = 24.dpScaled,
								selectedShowXAxisLine = false,
								unSelectedShowXAxisLine = true,
								selectedXAxisLineAnimationSpec = spring(
									stiffness = Spring.StiffnessVeryLow,
									dampingRatio = Spring.DampingRatioHighBouncy
								)
							)
						),
						chartStyle = BarChartDefault.barChartStyle(
							xAxisVisibility = XAxisVisibility.Joined
						),
						modifier = Modifier
							.padding(
								horizontal = 12.dpScaled
							)
					)
				}
				
				BudgetCard(
					totalExpense = totalAmountExpenseList,
					totalIncome = totalAmountIncomeList,
					modifier = Modifier
						.padding(
							vertical = 4.dpScaled,
							horizontal = 8.dpScaled
						)
				)
				
				Column(
					modifier = Modifier
						.padding(
							top = 8.dpScaled
						)
						.fillMaxWidth()
						.clip(
							extra_large_shape.copy(
								bottomStart = CornerSize(0.dpScaled),
								bottomEnd = CornerSize(0.dpScaled)
							)
						)
						.background(Color(0xFF2C383F))
				) {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally,
						modifier = Modifier
							.padding(16.dpScaled)
							.fillMaxWidth()
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.fillMaxWidth()
						) {
							Text(
								text = stringResource(
									id = R.string.income_for,
									AppUtil.longMonths[selectedBarDataGroup]
								),
								style = Typography.bodyMedium.copy(
									color = Color.White,
									fontWeight = FontWeight.SemiBold,
									fontSize = Typography.bodyMedium.fontSize.spScaled
								)
							)
							
							Spacer(
								modifier = Modifier
									.weight(1f)
							)
							
							Text(
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = incomeFinancialList.filter {
										chartViewModel.monthFormatter.format(
											it.dateCreated
										) == chartViewModel.monthFormatter.format(
											chartViewModel.getTimeInMillis(selectedBarDataGroup)
										)
									}.sumOf { it.amount },
									currencyCode = LocalCurrency.current.countryCode
								),
								style = Typography.titleMedium.copy(
									color = Color.White,
									fontWeight = FontWeight.Bold,
									fontSize = Typography.titleMedium.fontSize.spScaled
								),
								modifier = Modifier
									.padding(start = 16.dpScaled)
									.horizontalScroll(
										state = rememberScrollState(),
										autoRestart = true
									)
							)
						}
						
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.fillMaxWidth()
								.padding(top = 8.dpScaled)
						) {
							Text(
								text = stringResource(
									id = R.string.expenses_for,
									AppUtil.longMonths[selectedBarDataGroup]
								),
								style = Typography.bodyMedium.copy(
									color = Color.White,
									fontWeight = FontWeight.SemiBold,
									fontSize = Typography.bodyMedium.fontSize.spScaled
								)
							)
							
							Spacer(
								modifier = Modifier
									.weight(1f)
							)
							
							Text(
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = expenseFinancialList.filter {
										chartViewModel.monthFormatter.format(
											it.dateCreated
										) == chartViewModel.monthFormatter.format(
											chartViewModel.getTimeInMillis(selectedBarDataGroup)
										)
									}.sumOf { it.amount },
									currencyCode = LocalCurrency.current.countryCode
								),
								style = Typography.titleMedium.copy(
									color = Color.White,
									fontWeight = FontWeight.Bold,
									fontSize = Typography.titleMedium.fontSize.spScaled
								),
								modifier = Modifier
									.padding(start = 16.dpScaled)
									.horizontalScroll(
										state = rememberScrollState(),
										autoRestart = true
									)
							)
						}
					}
					
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(24.dpScaled)
							.clip(
								extra_large_shape.copy(
									bottomStart = CornerSize(0.dpScaled),
									bottomEnd = CornerSize(0.dpScaled)
								)
							)
							.background(MaterialTheme.colorScheme.background)
					)
				}
			}
		}
		
		items(
			items = lazyListValue,
			key = { item: Financial -> item.hashCode() }
		) { financial ->
			SwipeableFinancialCard(
				financial = financial,
				onDismissToEnd = { onFinancialCardDismissToEnd(financial) },
				onCanDelete = { onFinancialCardCanDelete(financial) },
				onClick = { onFinancialCardClicked(financial) },
				modifier = Modifier
					.padding(horizontal = 8.dpScaled)
					.testTag("SwipeableFinancialCard")
			)
		}
	}
}
