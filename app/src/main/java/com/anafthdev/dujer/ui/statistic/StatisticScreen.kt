package com.anafthdev.dujer.ui.statistic

import android.graphics.Color
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anafthdev.dujer.R
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.merge
import com.anafthdev.dujer.foundation.extension.toColor
import com.anafthdev.dujer.foundation.window.dpScaled
import com.anafthdev.dujer.foundation.window.spScaled
import com.anafthdev.dujer.ui.statistic.component.*
import com.anafthdev.dujer.ui.statistic.data.PercentValueFormatter
import com.anafthdev.dujer.ui.theme.Typography
import com.anafthdev.dujer.uicomponent.FinancialTypeSelector
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
	val config = LocalConfiguration.current
	
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
	var isDataSetEmpty by remember { mutableStateOf(false) }
	
	val pieColors = remember { mutableStateListOf<Int>() }
	val financialsForSelectedCategory = remember(incomeTransaction, expenseTransaction, selectedCategory) {
		incomeTransaction.merge(expenseTransaction).filter { it.category.id == selectedCategory.id }
	}
	
	val totalIncomeAmount = remember(incomeTransaction) {
		incomeTransaction.sumOf { it.amount }
	}
	
	val totalExpenseAmount = remember(expenseTransaction) {
		expenseTransaction.sumOf { it.amount }
	}

	val pieDataSet = remember(pieEntries) {
		val isEntryEmpty = pieEntries.isEmpty()
		val entries = pieEntries.ifEmpty { listOf(PieEntry(100f)) }
		
		isDataSetEmpty = isEntryEmpty
		
		pieColors.clear()
		pieColors.addAll(
			if (!isEntryEmpty) ColorTemplate.getRandomColor(pieEntries.size)
			else listOf(Color.GRAY)
		)
		
		PieDataSet(entries, "").apply {
			valueTextSize = 13f
			valueLineWidth = 2f
			selectionShift = 3f
			valueLinePart1Length = 0.6f
			valueLinePart2Length = 0.3f
			valueLinePart1OffsetPercentage = 115f  // Line starts outside of chart
			isUsingSliceColorAsValueLineColor = true
			
			colors = pieColors
			valueLineColor = Color.MAGENTA
			yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
			valueFormatter = PercentValueFormatter()
			valueTypeface = ResourcesCompat.getFont(
				context,
				R.font.inter_regular
			)
			
			setDrawValues(!isDataSetEmpty)
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
	
	LaunchedEffect(pieEntries) {
		if (pieEntries.isEmpty()) {
			selectedCategory = Category.default
			selectedPieColor = androidx.compose.ui.graphics.Color.Transparent
		}
	}
	
	LazyColumn(
		modifier = Modifier
			.systemBarsPadding()
			.fillMaxSize()
	) {
		item {
			Column {
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
					onDateChanged = { date ->
						statisticViewModel.dispatch(
							StatisticAction.SetSelectedDate(date)
						)
					},
					modifier = Modifier
						.align(Alignment.CenterHorizontally)
				)
				
				FinancialTypeSelector(
					selectedFinancialType = selectedFinancialType,
					onFinancialTypeChanged = { type ->
						statisticViewModel.dispatch(
							StatisticAction.SetSelectedFinancialType(type)
						)
					},
					modifier = Modifier
						.padding(
							horizontal = 8.dpScaled
						)
				)
				
				// TODO: Selected cateogryyy
				FinancialStatisticChart(
					dataSet = pieDataSet,
					isDataSetEmpty = isDataSetEmpty,
					category = selectedCategory,
					selectedColor = selectedPieColor,
					financialType = selectedFinancialType,
					financialsForSelectedCategory = financialsForSelectedCategory,
					onPieDataSelected = { entry: PieEntry?, highlight: Highlight? ->
						val category = if (availableCategory.isNotEmpty()) availableCategory[highlight?.x?.toInt() ?: -1]
						else Category.default
						
						selectedCategory = category
						selectedPieColor = try {
							pieColors[highlight?.x?.toInt() ?: -1].toColor()
						} catch (e: Exception) {
							// TODO: ganti warna
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
						.padding(8.dpScaled)
						.fillMaxWidth()
				)
				
				BalanceInfoCard(
					initialBalance = wallet.initialBalance,
					currentBalance = wallet.balance,
					modifier = Modifier
						.padding(8.dpScaled)
						.fillMaxWidth()
				)
				
				BalanceSummaryCard(
					totalIncome = totalIncomeAmount,
					totalExpense = totalExpenseAmount,
					modifier = Modifier
						.padding(8.dpScaled)
						.fillMaxWidth()
				)
				
				Spacer(modifier = Modifier.height(8.dpScaled))
			}
		}
		
		items(
			items = availableCategory,
			key = { item: Category -> item.hashCode() }
		) { category ->
			val financialList = remember(incomeTransaction, expenseTransaction) {
				(if (category.type == FinancialType.INCOME) incomeTransaction else expenseTransaction)
					.filter { it.category.id == category.id }
			}
			
			val totalAmount = remember(financialList) {
				financialList.sumOf { it.amount }
			}
			
			val percent = remember(financialList, totalIncomeAmount, totalExpenseAmount) {
				statisticViewModel.percentDecimalFormat.format(
					totalAmount
						.div(
							if (category.type == FinancialType.INCOME) totalIncomeAmount
							else totalExpenseAmount
						)
						.times(100)
				)
			}
			
			CategoryFinancialCard(
				category = category,
				percent = percent,
				totalAmount = totalAmount,
				financialList = financialList
			)
		}
	}
}
