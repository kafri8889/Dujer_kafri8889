package com.anafthdev.dujer.ui.screen.income

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.component.FinancialCard
import com.anafthdev.dujer.ui.component.TopAppBar
import com.anafthdev.dujer.ui.theme.*
import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.view.LineChartMarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IncomeScreen(
	navController: NavController
) {
	
	val context = LocalContext.current
	val config = LocalConfiguration.current
	
	val incomeViewModel = hiltViewModel<IncomeViewModel>()
	
	val currentCurrency by incomeViewModel.datastore.getCurrentCurrency.collectAsState(initial = Currency.INDONESIAN)
	val userBalance by incomeViewModel.datastore.getUserBalance.collectAsState(initial = 0.0)
	val incomeFinancialList by incomeViewModel.incomeFinancialList.observeAsState(initial = emptyList())
	
	val incomeLineDataSetEntry by rememberUpdatedState(
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
	val incomeLineDataset by rememberUpdatedState(
		newValue = LineDataSet(incomeLineDataSetEntry, "Income")
	)
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
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
				text = stringResource(id = R.string.my_income),
				style = Typography.titleLarge.copy(
					fontFamily = Inter,
					fontWeight = FontWeight.Bold,
					fontSize = Typography.titleLarge.fontSize.spScaled
				)
			)
		}
		
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.horizontalScroll(rememberScrollState())
		) {
			AndroidView(
				factory = { context ->
					LineChart(context)
				},
				update = { lineChart ->
					lineChart.extraBottomOffset = 8f
					lineChart.isDragEnabled = false
					lineChart.description.isEnabled = false
					lineChart.axisRight.isEnabled = false
					lineChart.legend.isEnabled = false
					lineChart.setDrawGridBackground(false)
					lineChart.setPinchZoom(false)
					lineChart.setScaleEnabled(false)
					
					val yAxisLeft = lineChart.axisLeft
					yAxisLeft.textColor = android.graphics.Color.BLACK
					yAxisLeft.textSize = 14f
					yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
					yAxisLeft.setDrawGridLines(true)
					yAxisLeft.enableGridDashedLine(12f, 12f, 0f)
					yAxisLeft.axisLineColor = android.graphics.Color.TRANSPARENT
					
					val xAxis = lineChart.xAxis
					xAxis.position = XAxis.XAxisPosition.BOTTOM
					xAxis.textColor = android.graphics.Color.BLACK
					xAxis.textSize = 14f
					xAxis.setLabelCount(AppUtil.shortMonths.size, true)
					xAxis.axisLineColor = black09.toArgb()
					xAxis.valueFormatter = IndexAxisValueFormatter(AppUtil.shortMonths)
					xAxis.setDrawGridLines(false)
					xAxis.setCenterAxisLabels(false)
					
					incomeLineDataset.lineWidth = 2.5f
					incomeLineDataset.cubicIntensity = .25f
					incomeLineDataset.mode = LineDataSet.Mode.CUBIC_BEZIER
					incomeLineDataset.color = android.graphics.Color.parseColor("#81B2CA")
					incomeLineDataset.setDrawValues(false)
					incomeLineDataset.setDrawFilled(false)
					incomeLineDataset.setDrawCircles(false)
					incomeLineDataset.setCircleColor(android.graphics.Color.parseColor("#81B2CA"))
					incomeLineDataset.setDrawHorizontalHighlightIndicator(false)
					incomeLineDataset.setFillFormatter { _, _ -> return@setFillFormatter lineChart.axisLeft.axisMinimum }
					
					lineChart.marker = LineChartMarkerView(context, currentCurrency)
					lineChart.data = LineData(incomeLineDataset)
					lineChart.invalidate()
				},
				modifier = Modifier
					.width(config.smallestScreenWidthDp.times(2).dpScaled)
					.height(
						config.smallestScreenWidthDp
							.times(2)
							.times(.34f).dpScaled
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
//				.height(config.screenHeightDp.dpScaled)
				.clip(
					extra_big_shape.copy(
						bottomStart = CornerSize(0.dpScaled),
						bottomEnd = CornerSize(0.dpScaled)
					)
				)
				.background(Color(0xFF2C383F))
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.padding(32.dpScaled)
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
							fontFamily = Inter,
							fontWeight = FontWeight.SemiBold,
							fontSize = Typography.bodyMedium.fontSize.spScaled
						)
					)
					
					Spacer(
						modifier = Modifier
							.weight(1f)
					)
					
					Text(
						text = "${currentCurrency.symbol} $userBalance",
						style = Typography.titleMedium.copy(
							color = Color.White,
							fontFamily = Inter,
							fontWeight = FontWeight.Bold,
							fontSize = Typography.titleMedium.fontSize.spScaled
						)
					)
				}
			}
			
			Column(
				modifier = Modifier
					.fillMaxSize()
					.clip(
						extra_big_shape.copy(
							bottomStart = CornerSize(0.dpScaled),
							bottomEnd = CornerSize(0.dpScaled)
						)
					)
					.background(MaterialTheme.colorScheme.background)
			) {
				Text(
					text = stringResource(id = R.string.your_income),
					style = Typography.bodyMedium.copy(
						color = black01,
						fontFamily = Inter,
						fontWeight = FontWeight.SemiBold,
						fontSize = Typography.bodyMedium.fontSize.spScaled
					),
					modifier = Modifier
						.padding(
							horizontal = 16.dpScaled,
							vertical = 14.dpScaled
						)
				)
				
				incomeFinancialList.forEach { financial ->
					FinancialCard(
						financial = financial,
						onClick = {
						
						},
						modifier = Modifier
							.padding(horizontal = 8.dpScaled)
							.fillMaxWidth()
					)
				}
			}
		}
	}
}
