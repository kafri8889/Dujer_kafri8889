package com.anafthdev.dujer.feature.budget.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import com.anafthdev.dujer.data.FinancialChartValueFormatter
import com.anafthdev.dujer.feature.budget.data.CustomBarChartRenderer
import com.anafthdev.dujer.feature.theme.black04
import com.anafthdev.dujer.feature.theme.black09
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun ExpensesBarChart(
	barEntries: List<BarEntry>,
	modifier: Modifier = Modifier
) {
	
	val uiMode = LocalUiMode.current
	val config = LocalConfiguration.current
	val contentColor = LocalContentColor.current
	
	val barData = remember(barEntries) {
		BarData(
			BarDataSet(barEntries, "Budget").apply {
				valueTextColor = contentColor.toArgb()
				valueTextSize = 14f
				valueFormatter = FinancialChartValueFormatter()
			}
		)
	}
	
	Column(
		modifier = modifier
			.fillMaxWidth()
			.horizontalScroll(
				rememberScrollState(),
				enabled = true
			)
	) {
		AndroidView(
			factory = { context ->
				BarChart(context).apply {
					xAxis.apply {
						axisMinimum = 0f
						position = XAxis.XAxisPosition.BOTTOM
						axisLineColor = android.graphics.Color.TRANSPARENT
						valueFormatter = IndexAxisValueFormatter(AppUtil.shortMonths)
						
						setAvoidFirstLastClipping(false)
						setDrawGridLines(false)
						setCenterAxisLabels(false)
						labelCount = AppUtil.shortMonths.size
					}
					
					axisLeft.apply {
						xOffset = 24f
						axisMinimum = 0f
						axisLineColor = android.graphics.Color.TRANSPARENT
						valueFormatter = FinancialChartValueFormatter()
						
						setDrawGridLines(true)
						enableGridDashedLine(
							12f,
							12f,
							0f
						)
					}
					
					legend.isEnabled = false
					axisRight.isEnabled = false
					description.isEnabled = false
					renderer = CustomBarChartRenderer(
						this,
						animator,
						viewPortHandler
					).apply {
						setRadius(24)
					}
					
					setPinchZoom(false)
					setScaleEnabled(false)
					setTouchEnabled(false)
					setDrawBarShadow(false)
					setDrawGridBackground(false)
				}
			},
			update = { barChart ->
				barChart.axisLeft.apply {
					textColor = contentColor.toArgb()
					gridColor = if (uiMode.isDarkTheme()) black09.toArgb() else black04.toArgb()
				}
				
				barChart.xAxis.apply {
					textColor = contentColor.toArgb()
				}
				
				barChart.data = barData
				
				barChart.invalidate()
			},
			modifier = Modifier
				.widthIn(
					min = config.smallestScreenWidthDp
						.times(2)
						.dpScaled
				)
				.height(
					config.smallestScreenWidthDp
						.times(2)
						.times(.4f)
						.dpScaled
				)
		)
	}

}
