package com.anafthdev.dujer.ui.income_expense

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialLineChartValueFormatter
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.extension.toArray
import com.anafthdev.dujer.foundation.uiextension.horizontalScroll
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.ui.app.DujerAction
import com.anafthdev.dujer.ui.app.DujerViewModel
import com.anafthdev.dujer.ui.financial.FinancialScreen
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.ui.theme.*
import com.anafthdev.dujer.uicomponent.SwipeableFinancialCard
import com.anafthdev.dujer.uicomponent.TopAppBar
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import com.anafthdev.dujer.view.SingleLineChartMarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IncomeExpenseScreen(
	navController: NavController,
	type: FinancialType,
	dujerViewModel: DujerViewModel
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	val config = LocalConfiguration.current
	val localCurrency = LocalCurrency.current
	val contentColor = LocalContentColor.current
	
	val incomeExpenseViewModel = hiltViewModel<IncomeExpenseViewModel>()
	
	val scope = rememberCoroutineScope { Dispatchers.Main }
	val financialScreenSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		skipHalfExpanded = true
	)
	
	val state by incomeExpenseViewModel.state.collectAsState()
	
	val financial = state.financial
	val incomeFinancialList = state.incomeFinancialList
	val expenseFinancialList = state.expenseFinancialList
	
	val incomeBalance by rememberUpdatedState(
		newValue = incomeFinancialList.getBy { it.amount }.sum()
	)
	val expenseBalance by rememberUpdatedState(
		newValue = expenseFinancialList.getBy { it.amount }.sum()
	)
	
	val hideSheet = {
		scope.launch { financialScreenSheetState.hide() }
		Unit
	}
	
	val showSheet = {
		scope.launch { financialScreenSheetState.show() }
		Unit
	}
	
	val incomeExpenseLineDataSetEntry by rememberUpdatedState(
		newValue = arrayListOf<Entry>().apply {
			// TODO: kalkulasi datanya
//			add(Entry(0f, 0f))
//			add(Entry(1f, 0f))
//			add(Entry(2f, 0f))
//			add(Entry(3f, 0f))
//			add(Entry(4f, 0f))
//			add(Entry(5f, 0f))
//			add(Entry(6f, 0f))
//			add(Entry(7f, 0f))
//			add(Entry(8f, 0f))
//			add(Entry(9f, 0f))
//			add(Entry(10f, 0f))
//			add(Entry(11f, 0f))
			
			add(Entry(0f, 2200f))
			add(Entry(1f, 1000f))
			add(Entry(2f, 3500f))
			add(Entry(3f, 0f))
			add(Entry(4f, 9500f))
			add(Entry(5f, 4000f))
			add(Entry(6f, 10000f))
			add(Entry(7f, 12000f))
			add(Entry(8f, 6400f))
			add(Entry(9f, 15200f))
			add(Entry(10f, 7000f))
			add(Entry(11f, 3500f))
		}
	)
	
	val incomeExpenseLineDataset by rememberUpdatedState(
		newValue = LineDataSet(
			incomeExpenseLineDataSetEntry,
			context.getString(
				if (type == FinancialType.INCOME) R.string.income else R.string.expenses
			)
		).apply {
			lineWidth = 2.5f
			cubicIntensity = .25f
			mode = LineDataSet.Mode.CUBIC_BEZIER
			color = android.graphics.Color.parseColor("#81B2CA")
			setDrawValues(false)
			setDrawFilled(false)
			setDrawCircles(false)
			setDrawHorizontalHighlightIndicator(false)
			setCircleColor(android.graphics.Color.parseColor("#81B2CA"))
		}
	)
	
	BackHandler(
		enabled = financialScreenSheetState.isVisible,
		onBack = hideSheet
	)
	
	ModalBottomSheetLayout(
		scrimColor = Color.Unspecified,
		sheetState = financialScreenSheetState,
		sheetContent = {
			FinancialScreen(
				financial = financial,
				financialAction = FinancialAction.EDIT,
				onBack = hideSheet,
				onSave = hideSheet
			)
		}
	) {
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
						.background(MaterialTheme.colorScheme.background)
				) {
					TopAppBar {
						IconButton(
							onClick = {
								navController.popBackStack()
							},
							modifier = Modifier
								.padding(start = 8.dpScaled)
								.align(Alignment.CenterStart)
						) {
							Icon(
								imageVector = Icons.Rounded.ArrowBack,
								tint = black04,
								contentDescription = null
							)
						}
						
						Text(
							text = stringResource(
								id = if (type == FinancialType.INCOME) R.string.my_income else R.string.my_spending
							),
							style = Typography.titleLarge.copy(
								fontWeight = FontWeight.Bold,
								fontSize = Typography.titleLarge.fontSize.spScaled
							)
						)
					}
					
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.horizontalScroll(
								rememberScrollState(),
								enabled = true
							)
					) {
						AndroidView(
							factory = { context ->
								LineChart(context).apply {
									extraBottomOffset = 16f
									isDragEnabled = false
									description.isEnabled = false
									axisRight.isEnabled = false
									legend.isEnabled = false
									setDrawGridBackground(false)
									setPinchZoom(false)
									setScaleEnabled(false)
									
									val yAxisLeft = axisLeft
									yAxisLeft.textSize = 14f
									yAxisLeft.axisMinimum = 0f
									yAxisLeft.axisLineColor = android.graphics.Color.TRANSPARENT
									yAxisLeft.valueFormatter = FinancialLineChartValueFormatter(localCurrency.countryCode)
									yAxisLeft.setDrawGridLines(true)
									yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
									yAxisLeft.enableGridDashedLine(12f, 12f, 0f)
									
									xAxis.yOffset = 16f
									xAxis.textSize = 14f
									xAxis.position = XAxis.XAxisPosition.BOTTOM
									xAxis.setLabelCount(AppUtil.shortMonths.size, true)
									xAxis.axisLineColor = android.graphics.Color.TRANSPARENT
									xAxis.valueFormatter = IndexAxisValueFormatter(AppUtil.shortMonths)
									xAxis.setDrawGridLines(false)
									xAxis.setCenterAxisLabels(false)
									
									incomeExpenseLineDataset.setFillFormatter { _, _ -> return@setFillFormatter axisLeft.axisMinimum }
								}
							},
							update = { lineChart ->
								val xAxis = lineChart.xAxis
								xAxis.textColor = contentColor.toArgb()
								
								val yAxisLeft = lineChart.axisLeft
								yAxisLeft.textColor = contentColor.toArgb()
								yAxisLeft.gridColor = if (uiMode.isDarkTheme()) black09.toArgb() else black04.toArgb()
								
								lineChart.marker = SingleLineChartMarkerView(context, localCurrency)
								lineChart.data = LineData(incomeExpenseLineDataset)
								lineChart.invalidate()
							},
							modifier = Modifier
								.width(
									config.smallestScreenWidthDp
										.times(2)
										.dpScaled
								)
								.height(
									config.smallestScreenWidthDp
										.times(2)
										.times(.4f)
										.dpScaled
								)
								.padding(
									horizontal = 8.dpScaled
								)
						)
					}
					
					Column(
						modifier = Modifier
							.padding(
								top = 8.dpScaled
							)
							.fillMaxSize()
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
								.padding(24.dpScaled)
								.fillMaxWidth()
						) {
							Row(
								verticalAlignment = Alignment.CenterVertically,
								modifier = Modifier
									.fillMaxWidth()
							) {
								Text(
									text = stringResource(
										id = if (type == FinancialType.INCOME) R.string.income_for else R.string.expenses_for,
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
										amount = if (type == FinancialType.INCOME) incomeBalance else expenseBalance,
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
						
						Column(
							modifier = Modifier
								.fillMaxSize()
								.clip(
									extra_large_shape.copy(
										bottomStart = CornerSize(0.dpScaled),
										bottomEnd = CornerSize(0.dpScaled)
									)
								)
								.background(MaterialTheme.colorScheme.background)
						) {
							Text(
								text = stringResource(
									id = if (type == FinancialType.INCOME) R.string.your_income else R.string.your_expenses
								),
								style = Typography.bodyMedium.copy(
									color = black01,
									fontWeight = FontWeight.SemiBold,
									fontSize = Typography.bodyMedium.fontSize.spScaled
								),
								modifier = Modifier
									.padding(
										horizontal = 16.dpScaled,
										vertical = 14.dpScaled
									)
							)
						}
					}
				}
			}
			
			items(
				items = if (type == FinancialType.INCOME) incomeFinancialList else expenseFinancialList,
				key = { item: Financial -> item.id }
			) { financial ->
				SwipeableFinancialCard(
					financial = financial,
					onDismissToEnd = {
						incomeExpenseViewModel.dispatch(
							IncomeExpenseAction.DeleteFinancial(financial.toArray())
						)
					},
					onCanDelete = {
						dujerViewModel.dispatch(
							DujerAction.Vibrate(100)
						)
					},
					onClick = {
						incomeExpenseViewModel.dispatch(
							IncomeExpenseAction.SetFinancialID(financial.id)
						)
						
						showSheet()
					},
					modifier = Modifier
						.testTag("SwipeableFinancialCard")
				)
			}
		}
	}
}
