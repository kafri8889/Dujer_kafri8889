package com.anafthdev.dujer.feature.chart

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
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.app.LocalDujerState
import com.anafthdev.dujer.feature.chart.data.MonthBarChartFormatter
import com.anafthdev.dujer.feature.theme.*
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.common.AppUtil.yearFormatter
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.merge
import com.anafthdev.dujer.foundation.extension.replace
import com.anafthdev.dujer.foundation.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.foundation.uicomponent.YearSelector
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.BarChart
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.components.XAxisVisibility
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.data.BarChartDefault
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.data.BarDataSet
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.rememberBarChartState
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChartScreen(
	onFinancialCardDismissToEnd: (Financial) -> Unit,
	onFinancialCardClicked: (Financial) -> Unit
) {
	
	val config = LocalConfiguration.current
	val density = LocalDensity.current
	val dujerState = LocalDujerState.current
	
//	val barDataSets = listOf(
//		BarDataSet(BarData.sample1),
//		BarDataSet(BarData.sample2)
//	)
	
	val chartViewModel = hiltViewModel<ChartViewModel>()
	
	val state by chartViewModel.state.collectAsState()
	
	val incomeTransaction = dujerState.allIncomeTransaction
	val expenseTransaction = dujerState.allExpenseTransaction
	
	val incomeBarDataList = remember { mutableStateListOf<BarData>() }
	val expenseBarDataList = remember { mutableStateListOf<BarData>() }
	
	var selectedYear by remember { mutableStateOf(System.currentTimeMillis()) }
	var selectedBarDataGroup by remember { mutableStateOf(Calendar.getInstance()[Calendar.MONTH]) }
	
	val lazyListValue = remember(
		selectedYear,
		selectedBarDataGroup,
		incomeTransaction,
		expenseTransaction
	) {
		incomeTransaction.merge(expenseTransaction)
			.asSequence()
			.filter { yearFormatter.format(it.dateCreated) == yearFormatter.format(selectedYear) }
			.filter {
			chartViewModel.monthFormatter.format(
				it.dateCreated
			) == chartViewModel.monthFormatter.format(chartViewModel.getTimeInMillis(selectedBarDataGroup))
		}.toList()
	}
	
	val incomeAmount = remember(
		selectedYear,
		selectedBarDataGroup,
		incomeTransaction
	) {
		incomeTransaction
			.asSequence()
			.filter { yearFormatter.format(it.dateCreated) == yearFormatter.format(selectedYear) }
			.filter {
				chartViewModel.monthFormatter.format(
					it.dateCreated
				) == chartViewModel.monthFormatter.format(chartViewModel.getTimeInMillis(selectedBarDataGroup))
			}.sumOf { it.amount }
	}
	
	val expenseAmount = remember(
		selectedYear,
		selectedBarDataGroup,
		expenseTransaction
	) {
		expenseTransaction
			.asSequence()
			.filter { yearFormatter.format(it.dateCreated) == yearFormatter.format(selectedYear) }
			.filter {
				chartViewModel.monthFormatter.format(
					it.dateCreated
				) == chartViewModel.monthFormatter.format(chartViewModel.getTimeInMillis(selectedBarDataGroup))
			}.sumOf { it.amount }
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
		val (filteredIncome, filteredExpense) = chartViewModel.filter(
			yearInMillis = selectedYear,
			incomeList = incomeTransaction,
			expenseList = expenseTransaction
		)
		
		val (incomeBarData, expenseBarData) = chartViewModel.calculateBarData(
			incomeList = filteredIncome,
			expenseList = filteredExpense
		)
		
		incomeBarDataList.replace(incomeBarData)
		expenseBarDataList.replace(expenseBarData)
		
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
								selectedBarColor = income_color,
								selectedStartPaddingBarContainer = 24.dpScaled,
								selectedShowXAxisLine = false,
								unSelectedShowXAxisLine = true,
								selectedXAxisLineAnimationSpec = spring(
									stiffness = Spring.StiffnessVeryLow,
									dampingRatio = Spring.DampingRatioHighBouncy
								),
								selectedYAxisTextStyle = MaterialTheme.typography.bodySmall.copy(
									color = black10,
									fontSize = MaterialTheme.typography.bodySmall.fontSize.spScaled
								),
								unSelectedYAxisTextStyle = MaterialTheme.typography.bodySmall.copy(
									color = black10,
									fontSize = MaterialTheme.typography.bodySmall.fontSize.spScaled
								)
							),
							BarChartDefault.barStyle(
								selectedBarColor = expense_color,
								selectedEndPaddingBarContainer = 24.dpScaled,
								selectedShowXAxisLine = false,
								unSelectedShowXAxisLine = true,
								selectedXAxisLineAnimationSpec = spring(
									stiffness = Spring.StiffnessVeryLow,
									dampingRatio = Spring.DampingRatioHighBouncy
								),
								selectedYAxisTextStyle = MaterialTheme.typography.bodySmall.copy(
									color = black10,
									fontSize = MaterialTheme.typography.bodySmall.fontSize.spScaled
								),
								unSelectedYAxisTextStyle = MaterialTheme.typography.bodySmall.copy(
									color = black10,
									fontSize = MaterialTheme.typography.bodySmall.fontSize.spScaled
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
									amount = incomeAmount,
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
									amount = expenseAmount,
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
				onClick = { onFinancialCardClicked(financial) },
				onDismissToEnd = { onFinancialCardDismissToEnd(financial) },
				modifier = Modifier
					.padding(horizontal = 8.dpScaled)
					.testTag("SwipeableFinancialCard")
			)
		}
	}
}
