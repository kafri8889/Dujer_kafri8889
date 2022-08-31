package com.anafthdev.dujer.feature.chart

import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.chart.environment.IChartEnvironment
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.foundation.extension.isPositive
import com.anafthdev.dujer.foundation.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
	chartEnvironment: IChartEnvironment
): StatefulViewModel<ChartState, Unit, ChartAction, IChartEnvironment>(
	ChartState,
	chartEnvironment
) {
	
	val monthFormatter = SimpleDateFormat("MMM", deviceLocale)
	val yearFormatter = SimpleDateFormat("yyyy", deviceLocale)
	
	private val calendar = Calendar.getInstance()
	
	override fun dispatch(action: ChartAction) {}
	
	fun filter(
		yearInMillis: Long,
		incomeList: List<Financial>,
		expenseList: List<Financial>
	): Pair<List<Financial>, List<Financial>> {
		val filteredIncome = incomeList.filter {
			yearFormatter.format(
				it.dateCreated
			) == yearFormatter.format(
				yearInMillis
			)
		}
		
		val filteredExpense = expenseList.filter {
			yearFormatter.format(
				it.dateCreated
			) == yearFormatter.format(
				yearInMillis
			)
		}
		
		return filteredIncome to filteredExpense
	}
	
	fun calculateBarData(
		incomeList: List<Financial>,
		expenseList: List<Financial>
	): Pair<List<BarData>, List<BarData>> {
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
		
		return incomeBarData to expenseBarData
	}
	
	fun getTimeInMillis(month: Int): Long {
		return calendar.apply {
			this[Calendar.MONTH] = month
		}.timeInMillis
	}
	
}