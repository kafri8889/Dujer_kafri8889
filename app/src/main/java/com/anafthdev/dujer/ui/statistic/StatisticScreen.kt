package com.anafthdev.dujer.ui.statistic

import android.graphics.Color
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.statistic.component.FinancialStatisticChart
import com.anafthdev.dujer.ui.statistic.component.MonthYearSelector
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.TopAppBar
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
	
	var selectedCategory by remember { mutableStateOf(Category.default) }
	var selectedPieColor by remember { mutableStateOf(androidx.compose.ui.graphics.Color.Transparent) }
	
	val pieColors = remember { mutableStateListOf<Int>() }
	val categories = remember(availableCategory) { availableCategory }

	val pieDataSet = remember(pieEntries) {
		pieColors.clear()
		pieColors.addAll(ColorTemplate.getRandomColor(pieEntries.size))
		
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
			.systemBarsPadding()
			.fillMaxSize()
	) {
		TopAppBar {
			IconButton(
				onClick = {
					navController.popBackStack()
				},
				modifier = Modifier
					.align(Alignment.CenterStart)
			) {
				Icon(
					imageVector = Icons.Rounded.ArrowBack,
					contentDescription = null
				)
			}
			
			Text(
				text = stringResource(id = R.string.statistic),
				style = Typography.titleLarge.copy(
					fontWeight = FontWeight.Bold,
					fontSize = Typography.titleLarge.fontSize.spScaled
				)
			)
		}
		
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
			category = selectedCategory,
			selectedColor = selectedPieColor,
			financialType = selectedFinancialType,
			onPieDataSelected = { entry: PieEntry?, highlight: Highlight? ->
				val category = if (categories.isNotEmpty()) categories[highlight?.x?.toInt() ?: -1]
				else Category.default
				
				selectedCategory = category
				selectedPieColor = try {
					pieColors[highlight?.x?.toInt() ?: -1].toColor()
				} catch (e: Exception) {
					androidx.compose.ui.graphics.Color.Transparent
				}
				
				Timber.i("entry: $entry, highlight: $highlight")
				Timber.i("selected category: $category")
			},
			onNothingSelected = {
				selectedCategory = Category.default
				selectedPieColor = androidx.compose.ui.graphics.Color.Transparent
			},
			modifier = Modifier
				.fillMaxWidth()
				.height(256.dpScaled)
		)
	}
}
