package com.anafthdev.dujer.ui.chart

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.foundation.extension.isPositive
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.chart.environment.IChartEnvironment
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
	chartEnvironment: IChartEnvironment
): StatefulViewModel<ChartState, Unit, ChartAction, IChartEnvironment>(
	ChartState(),
	chartEnvironment
) {
	
	val monthFormatter = SimpleDateFormat("MMM", AppUtil.deviceLocale)
	val yearFormatter = SimpleDateFormat("yyyy", AppUtil.deviceLocale)
	
	private val calendar = Calendar.getInstance()
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getFilteredIncomeList()
				.combine(environment.getFilteredExpenseList()) { income, expense ->
					income to expense
				}.collect { pair ->
				setState {
					copy(
						incomeFinancialList = pair.first,
						expenseFinancialList = pair.second
					)
				}

				calculateBarData(pair.first, pair.second)
			}
		}
	}
	
	override fun dispatch(action: ChartAction) {
		when (action) {
			is ChartAction.GetData -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.getFilteredIncomeList(
						yearInMillis = action.yearInMillis
					)
					
					environment.getFilteredExpenseList(
						yearInMillis = action.yearInMillis
					)
				}
			}
		}
	}
	
	private fun calculateBarData(incomeList: List<Financial>, expenseList: List<Financial>) {
		val incomeBarData = arrayListOf<BarData>()
		val expenseBarData = arrayListOf<BarData>()
		
		for (i in 0 until 12) {
			incomeBarData.add(BarData(i.toFloat(), 0f))
			expenseBarData.add(BarData(i.toFloat(), 0f))
		}
		
		val groupedIncomeList = incomeList.groupBy { monthFormatter.format(it.dateCreated) }
		val groupedExpenseList = expenseList.groupBy { monthFormatter.format(it.dateCreated) }
		
		groupedIncomeList.forEachMap { k, v ->
			val barDataIndex = AppUtil.shortMonths.indexOf { it == k }
			
			if (barDataIndex.isPositive()) {
				incomeBarData[barDataIndex] = BarData(
					x = incomeBarData[barDataIndex].x,
					y = v.sumOf { it.amount }.toFloat()
				)
			}
		}
		
		groupedExpenseList.forEachMap { k, v ->
			val barDataIndex = AppUtil.shortMonths.indexOf { it == k }
			
			if (barDataIndex.isPositive()) {
				expenseBarData[barDataIndex] = BarData(
					x = expenseBarData[barDataIndex].x,
					y = v.sumOf { it.amount }.toFloat()
				)
			}
		}
		
		Timber.i("income: $incomeBarData, expense: $expenseBarData")
		
		viewModelScope.launch(environment.dispatcher) {
			setState {
				copy(
					incomeBarDataList = incomeBarData,
					expenseBarDataList = expenseBarData
				)
			}
		}
	}
	
	fun getTimeInMillis(month: Int): Long {
		return calendar.apply {
			this[Calendar.MONTH] = month
		}.timeInMillis
	}
	
}