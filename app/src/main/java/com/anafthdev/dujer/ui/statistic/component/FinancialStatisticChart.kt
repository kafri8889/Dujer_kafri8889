package com.anafthdev.dujer.ui.statistic.component

import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

@Composable
fun FinancialStatisticChart(
	dataSet: PieDataSet,
	financialType: FinancialType,
	modifier: Modifier = Modifier,
	onPieDataSelected: (Entry?) -> Category
) {
	
	val background = MaterialTheme.colorScheme.background
	
	var selectedCategory by remember { mutableStateOf(Category.default) }
	
	AndroidView(
		factory = { context ->
			PieChart(context).apply {
				rotationAngle = 0f
				isDrawHoleEnabled = true
				isRotationEnabled = true
				legend.isEnabled = false
				description.isEnabled = false
				isHighlightPerTapEnabled = true
				
				setUsePercentValues(true)
				setHoleColor(background.toArgb())
				setCenterTextTypeface(
					Typeface.createFromAsset(context.assets, "inter_regular.ttf")
				)
				
				setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
					override fun onValueSelected(e: Entry?, h: Highlight?) {
						selectedCategory = onPieDataSelected(e)
					}
					
					override fun onNothingSelected() {}
				})
			}
		},
		update = { pieChart ->
			pieChart.centerText = ""
			
			pieChart.data = PieData(dataSet)
			pieChart.invalidate()
		},
		modifier = modifier
	)
}
