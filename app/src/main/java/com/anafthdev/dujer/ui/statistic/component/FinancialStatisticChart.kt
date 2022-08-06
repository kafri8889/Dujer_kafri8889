package com.anafthdev.dujer.ui.statistic.component

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.isDarkTheme
import com.anafthdev.dujer.foundation.extension.isDefault
import com.anafthdev.dujer.foundation.uimode.data.LocalUiMode
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.statistic.data.CustomPieChartRenderer
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialStatisticChart(
	isDataSetEmpty: Boolean,
	dataSet: PieDataSet,
	category: Category,
	selectedColor: androidx.compose.ui.graphics.Color,
	modifier: Modifier = Modifier,
	onPieDataSelected: (Highlight, Int) -> Unit,
	onStatisticCardClicked: () -> Unit,
	onNothingSelected: () -> Unit
) {
	
	val uiMode = LocalUiMode.current
	val context = LocalContext.current
	
	val pieLabelColor = if (uiMode.isDarkTheme()) Color.WHITE else Color.BLACK
	val background = MaterialTheme.colorScheme.background
	
	var selectedCategory by remember { mutableStateOf(Category.default) }
	var selectedEntry by remember {
		mutableStateOf(
			PieEntry(0f, "", Category.default.id to 0.0)
		)
	}
	var showSelectedCategory by remember { mutableStateOf(false) }
	
	val pieChart = remember {
		PieChart(context)
	}
	val pieChartRenderer = remember {
		CustomPieChartRenderer(
			pieChart = pieChart,
			circleRadius = 10f
		)
	}
	
	LaunchedEffect(category) {
		selectedCategory = category
		showSelectedCategory = !category.isDefault()
	}
	
	LaunchedEffect(dataSet) {
		selectedCategory = Category.default
		showSelectedCategory = false
		
		pieChart.highlightValues(null)
	}
	
	Card(
		shape = MaterialTheme.shapes.large,
		modifier = modifier
			.fillMaxWidth()
			.padding(4.dpScaled)
	) {
		AndroidView(
			factory = {
				pieChart.apply {
					holeRadius = 60f
					rotationAngle = 0f
					isDrawHoleEnabled = true
					isRotationEnabled = true
					legend.isEnabled = false
					description.isEnabled = false
					isHighlightPerTapEnabled = true
					
					setExtraOffsets(0f, 16f, 0f, 16f)
					setEntryLabelColor(pieLabelColor)
					setDrawEntryLabels(false)
					setUsePercentValues(true)
					setCenterTextTypeface(
						ResourcesCompat.getFont(
							context,
							R.font.inter_regular
						)
					)
				}
			},
			update = { mPieChart ->
				mPieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
					override fun onValueSelected(e: Entry, h: Highlight) {
						selectedEntry = e as PieEntry
						try {
							onPieDataSelected(
								h,
								(selectedEntry.data as Pair<*, *>).first as Int
							)
						} catch (e: Exception) {
							Timber.i("no data")
						}
					}
					
					override fun onNothingSelected() {
						onNothingSelected()
					}
				})
				
				mPieChart.renderer = pieChartRenderer.setCircleRadius(
					if (!isDataSetEmpty) 10f else 0f
				)
				
				mPieChart.data = PieData(dataSet)
				mPieChart.centerText = SpannableString(if (isDataSetEmpty) context.getString(R.string.no_data) else "").apply {
					setSpan(ForegroundColorSpan(pieLabelColor), 0, this.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
				}
				
				mPieChart.setHoleColor(background.toArgb())
				mPieChart.invalidate()
			},
			modifier = Modifier
				.padding(
					vertical = 8.dpScaled
				)
				.fillMaxWidth()
				.heightIn(
					min = 256.dpScaled
				)
		)
		
		AnimatedVisibility(
			visible = showSelectedCategory,
			enter = fadeIn(
				animationSpec = tween(200)
			) + expandVertically(
				animationSpec = tween(600)
			),
			exit = fadeOut(
				animationSpec = tween(200)
			) + shrinkVertically(
				animationSpec = tween(600)
			),
			modifier = Modifier
				.padding(16.dpScaled)
		) {
			StatisticCategoryCard(
				color = selectedColor,
				category = selectedCategory,
				totalAmount = (selectedEntry.data as Pair<*, *>).second as Double,
				onClick = onStatisticCardClicked
			)
		}
	}
}
