package com.anafthdev.dujer.ui.statistic.component

import android.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.ui.statistic.data.CustomPieChartRenderer
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import timber.log.Timber

@Composable
fun FinancialStatisticChart(
	dataSet: PieDataSet,
	financialType: FinancialType,
	selectedCategory: Category,
	modifier: Modifier = Modifier,
	onPieDataSelected: (PieEntry?, Highlight?) -> Unit
) {
	
	val background = MaterialTheme.colorScheme.background
	
	Timber.i("selectedCategory: $selectedCategory")
	
	AndroidView(
		factory = { context ->
			PieChart(context).apply {
				holeRadius = 60f
				rotationAngle = 0f
				isDrawHoleEnabled = true
				isRotationEnabled = true
				legend.isEnabled = false
				description.isEnabled = false
				isHighlightPerTapEnabled = true
				renderer = CustomPieChartRenderer(this, 10f)
				
				setExtraOffsets(0f, 16f, 0f, 16f)
				setEntryLabelColor(Color.BLACK)
				setDrawEntryLabels(false)
				setUsePercentValues(true)
				setHoleColor(background.toArgb())
				setCenterTextTypeface(
					ResourcesCompat.getFont(
						context,
						R.font.inter_regular
					)
				)
			}
		},
		update = { pieChart ->
			pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
				override fun onValueSelected(e: Entry?, h: Highlight?) {
					Timber.i("selekted entri: $e, $h")
					onPieDataSelected((e as PieEntry), h)
				}
				
				override fun onNothingSelected() {}
			})
			
			pieChart.centerText = ""
			
			pieChart.data = PieData(dataSet)
			pieChart.invalidate()
		},
		modifier = modifier
	)
}
