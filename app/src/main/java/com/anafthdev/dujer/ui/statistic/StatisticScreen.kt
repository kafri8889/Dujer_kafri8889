package com.anafthdev.dujer.ui.statistic

import android.graphics.Color
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.ui.statistic.component.FinancialStatisticChart
import com.anafthdev.dujer.ui.statistic.component.MonthYearSelector
import com.anafthdev.dujer.util.ColorTemplate
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import timber.log.Timber

@Composable
fun StatisticScreen(
	walletID: Int,
	navController: NavController
) {
	
	val context = LocalContext.current
	
	val statisticViewModel = hiltViewModel<StatisticViewModel>()
	
	val state by statisticViewModel.state.collectAsState()
	
	val wallet = state.wallet
	val pieEntries = state.pieEntries
	val incomeTransaction = state.incomeTransaction
	val expenseTransaction = state.expenseTransaction
	val availableCategory = state.availableCategory
	val selectedFinancialType = state.selectedFinancialType
	
	val categories = remember(availableCategory) { availableCategory }

	val pieDataSet = remember(pieEntries) {
		val pieColors = ColorTemplate.getRandomColor(pieEntries.size)
		
		PieDataSet(pieEntries, "").apply {
			valueLinePart1Length = 0.6f
			valueLinePart2Length = 0.3f
			valueLineWidth = 2f
			selectionShift = 3f
			valueLinePart1OffsetPercentage = 115f  // Line starts outside of chart
			isUsingSliceColorAsValueLineColor = true
			
			colors = pieColors
			valueLineColor = Color.MAGENTA
			yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
			valueTypeface = ResourcesCompat.getFont(
				context,
				R.font.inter_regular
			)
		}
	}
	
	BackHandler {
		navController.popBackStack()
	}
	
	LaunchedEffect(walletID) {
		statisticViewModel.dispatch(
			StatisticAction.Get(walletID)
		)
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		MonthYearSelector(
			onDateChanged = {
			
			},
			modifier = Modifier
				.padding(16.dpScaled)
				.align(Alignment.CenterHorizontally)
		)
		Timber.i("categories: $availableCategory")
		
		// TODO: Selected cateogryyy
		FinancialStatisticChart(
			dataSet = pieDataSet,
			selectedCategory = Category.default,
			financialType = selectedFinancialType,
			onPieDataSelected = { entry: PieEntry?, highlight: Highlight? ->
				Timber.i("entry: $entry, highlight: $highlight")
				Timber.i("category: ${
					if (categories.isNotEmpty()) categories[highlight?.x?.toInt() ?: -1] else ""
				}")
			},
			modifier = Modifier
				.fillMaxWidth()
				.height(256.dpScaled)
		)
	}
}
