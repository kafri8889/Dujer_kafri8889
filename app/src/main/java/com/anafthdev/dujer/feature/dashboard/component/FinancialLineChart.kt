package com.anafthdev.dujer.feature.dashboard.component

import android.graphics.Color
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.anafthdev.dujer.data.FinancialChartValueFormatter
import com.anafthdev.dujer.feature.theme.black04
import com.anafthdev.dujer.feature.theme.black09
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.model.LocalCurrency
import com.anafthdev.dujer.view.MultiLineChartMarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun FinancialLineChart(
	incomeLineDataset: LineDataSet,
	expenseLineDataset: LineDataSet
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	val config = LocalConfiguration.current
	val localCurrency = LocalCurrency.current
	val contentColor = LocalContentColor.current
	
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
					yAxisLeft.axisLineColor = Color.TRANSPARENT
					yAxisLeft.valueFormatter = FinancialChartValueFormatter()
					yAxisLeft.setDrawGridLines(true)
					yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
					yAxisLeft.enableGridDashedLine(12f, 12f, 0f)
					
					val xAxis = xAxis
					xAxis.yOffset = 16f
					xAxis.position = XAxis.XAxisPosition.BOTTOM
					xAxis.textSize = 14f
					xAxis.setLabelCount(AppUtil.shortMonths.size, true)
					xAxis.axisLineColor = Color.TRANSPARENT
					xAxis.valueFormatter = IndexAxisValueFormatter(AppUtil.shortMonths)
					xAxis.setDrawGridLines(false)
					xAxis.setCenterAxisLabels(false)
					
					incomeLineDataset.setFillFormatter { _, _ -> return@setFillFormatter axisLeft.axisMinimum }
					expenseLineDataset.setFillFormatter { _, _ -> return@setFillFormatter axisLeft.axisMinimum }
				}
			},
			update = { lineChart ->
				val xAxis = lineChart.xAxis
				xAxis.textColor = contentColor.toArgb()
				
				val yAxisLeft = lineChart.axisLeft
				yAxisLeft.textColor = contentColor.toArgb()
				yAxisLeft.gridColor = if (uiMode.isDarkTheme()) black09.toArgb() else black04.toArgb()
				
				lineChart.data = LineData(incomeLineDataset, expenseLineDataset)
				lineChart.marker = MultiLineChartMarkerView(
					context,
					localCurrency,
					incomeLineDataset.values,
					expenseLineDataset.values
				)
				
				lineChart.invalidate()
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
				.padding(
					horizontal = 8.dpScaled
				)
		)
	}
}
