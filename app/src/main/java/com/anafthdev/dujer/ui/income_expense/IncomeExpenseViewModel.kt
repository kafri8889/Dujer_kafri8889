package com.anafthdev.dujer.ui.income_expense

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.income_expense.environment.IIncomeExpenseEnvironment
import com.anafthdev.dujer.util.AppUtil
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
	incomeExpenseEnvironment: IIncomeExpenseEnvironment
): StatefulViewModel<IncomeExpenseState, Unit, IncomeExpenseAction, IIncomeExpenseEnvironment>(IncomeExpenseState(), incomeExpenseEnvironment) {
	
	val dummyEntry: List<Entry>
	get() = arrayListOf<Entry>().apply {
		add(Entry(0f, 2200f))
		add(Entry(1f, 1000f))
		add(Entry(2f, 3500f))
		add(Entry(3f, 0f))
		add(Entry(4f, 9500f))
		add(Entry(5f, 4000f))
		add(Entry(6f, 10000f))
		add(Entry(7f, 12000f))
		add(Entry(8f, 6400f))
		add(Entry(9f, 15200f))
		add(Entry(10f, 7000f))
		add(Entry(11f, 3500f))
	}
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getIncomeFinancialList()
				.combine(environment.getExpenseFinancialList()) { income, expense ->
					income to expense
				}.collect { pair ->
					calculateEntry(pair.first, pair.second)
					setState {
						copy(
							incomeFinancialList = pair.first,
							expenseFinancialList = pair.second
						)
					}
				}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancial().collect { financial ->
				setState {
					copy(
						financial = financial
					)
				}
			}
		}
	}
	
	override fun dispatch(action: IncomeExpenseAction) {
		when (action) {
			is IncomeExpenseAction.SetFinancialID -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFinancialID(action.id)
				}
			}
		}
	}
	
	private fun calculateEntry(incomeList: List<Financial>, expenseList: List<Financial>) {
		viewModelScope.launch(Dispatchers.Main) {
			val monthFormatter = SimpleDateFormat("MMM", AppUtil.deviceLocale)
			
			val incomeListEntry = arrayListOf<Entry>().apply {
				add(Entry(0f, 0f))
				add(Entry(1f, 0f))
				add(Entry(2f, 0f))
				add(Entry(3f, 0f))
				add(Entry(4f, 0f))
				add(Entry(5f, 0f))
				add(Entry(6f, 0f))
				add(Entry(7f, 0f))
				add(Entry(8f, 0f))
				add(Entry(9f, 0f))
				add(Entry(10f, 0f))
				add(Entry(11f, 0f))
			}
			
			val expenseListEntry = arrayListOf<Entry>().apply {
				add(Entry(0f, 0f))
				add(Entry(1f, 0f))
				add(Entry(2f, 0f))
				add(Entry(3f, 0f))
				add(Entry(4f, 0f))
				add(Entry(5f, 0f))
				add(Entry(6f, 0f))
				add(Entry(7f, 0f))
				add(Entry(8f, 0f))
				add(Entry(9f, 0f))
				add(Entry(10f, 0f))
				add(Entry(11f, 0f))
			}
			
			val monthGroupIncomeList = incomeList.groupBy { monthFormatter.format(it.dateCreated) }
			val monthGroupExpenseList = expenseList.groupBy { monthFormatter.format(it.dateCreated) }
			
			monthGroupIncomeList.forEachMap { k, v ->
				val entryIndex = AppUtil.shortMonths.indexOf { it.contentEquals(k, true) }
				
				incomeListEntry[entryIndex] = Entry(
					entryIndex.toFloat(),
					v.sumOf { it.amount }.toFloat()
				)
			}
			
			monthGroupExpenseList.forEachMap { k, v ->
				val entryIndex = AppUtil.shortMonths.indexOf { it.contentEquals(k, true) }
				
				expenseListEntry[entryIndex] = Entry(
					entryIndex.toFloat(),
					v.sumOf { it.amount }.toFloat()
				)
			}
			
			setState {
				copy(
					incomeLineChartEntry = incomeListEntry,
					expenseLineChartEntry = expenseListEntry
				)
			}
		}
	}
	
}