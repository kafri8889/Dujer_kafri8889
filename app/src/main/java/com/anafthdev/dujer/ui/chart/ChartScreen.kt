package com.anafthdev.dujer.ui.chart

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.chart.data.MonthBarChartFormatter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.ui.theme.expenseColor
import com.anafthdev.dujer.ui.theme.extra_large_shape
import com.anafthdev.dujer.ui.theme.incomeColor
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.charting.bar.BarChart
import com.anafthdev.dujer.uicomponent.charting.bar.components.XAxisVisibility
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarChartDefault
import com.anafthdev.dujer.uicomponent.charting.bar.data.BarDataSet
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import java.util.*

@Composable
fun ChartScreen(
	dashboardNavController: NavController,
	onFinancialCardDismissToEnd: (Financial) -> Unit,
	onFinancialCardCanDelete: (Financial) -> Unit,
	onFinancialCardClicked: (Financial) -> Unit
) {
	
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
				BarChart(
					formatter = MonthBarChartFormatter(),
					barDataSets = barDataSets,
//					barDataSets = listOf(
//						BarDataSet(incomeBarDataList),
//						BarDataSet(expenseBarDataList)
//					),
					style = listOf(
						BarChartDefault.barStyle(
							selectedBarColor = incomeColor,
							selectedStartPaddingBarContainer = 16.dpScaled,
							selectedShowXAxisLine = false,
							unSelectedShowXAxisLine = true,
							selectedXAxisLineAnimationSpec = spring(
								stiffness = Spring.StiffnessVeryLow,
								dampingRatio = Spring.DampingRatioHighBouncy
							)
						),
						BarChartDefault.barStyle(
							selectedBarColor = expenseColor,
							selectedEndPaddingBarContainer = 16.dpScaled,
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
									AppUtil.longMonths[Calendar.getInstance()[Calendar.MONTH]]
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
									locale = AppUtil.deviceLocale,
									amount = 0.0,
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
									AppUtil.longMonths[Calendar.getInstance()[Calendar.MONTH]]
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
									locale = AppUtil.deviceLocale,
									amount = 0.0,
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
			items = incomeFinancialList + expenseFinancialList,
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
