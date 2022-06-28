package com.anafthdev.dujer.ui.income_expense

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.income_expense.environment.IIncomeExpenseEnvironment
import com.anafthdev.dujer.util.AppUtil
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
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
			environment.getFinancial().collect { financial ->
				setState {
					copy(
						financial = financial
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getTransactions().collect { transactions ->
				setState {
					copy(
						transactions = transactions
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getSortType().collect { type ->
				setState {
					copy(
						sortType = type
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getGroupType().collect { type ->
				setState {
					copy(
						groupType = type
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getFilterDate().collect { date ->
				setState {
					copy(
						filterDate = date
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getSelectedMonth().collect { months ->
				setState {
					copy(
						selectedMonth = months
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
			is IncomeExpenseAction.SetSortType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSortType(action.sortType)
				}
			}
			is IncomeExpenseAction.SetSelectedMonth -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSelectedMonth(action.selectedMonth)
				}
			}
			is IncomeExpenseAction.SetFilterDate -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFilterDate(action.filterDate)
				}
			}
			is IncomeExpenseAction.SetGroupType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setGroupType(action.groupType)
				}
			}
		}
	}
	
	fun calculateEntry(incomeList: List<Financial>, expenseList: List<Financial>): Pair<List<Entry>, List<Entry>> {
		val monthFormatter = SimpleDateFormat("MMM", deviceLocale)
		
		val incomeListEntry = arrayListOf<Entry>().apply {
			add(Entry(0f, 0f, 0.0))
			add(Entry(1f, 0f, 0.0))
			add(Entry(2f, 0f, 0.0))
			add(Entry(3f, 0f, 0.0))
			add(Entry(4f, 0f, 0.0))
			add(Entry(5f, 0f, 0.0))
			add(Entry(6f, 0f, 0.0))
			add(Entry(7f, 0f, 0.0))
			add(Entry(8f, 0f, 0.0))
			add(Entry(9f, 0f, 0.0))
			add(Entry(10f, 0f, 0.0))
			add(Entry(11f, 0f, 0.0))
		}
		
		val expenseListEntry = arrayListOf<Entry>().apply {
			add(Entry(0f, 0f, 0.0))
			add(Entry(1f, 0f, 0.0))
			add(Entry(2f, 0f, 0.0))
			add(Entry(3f, 0f, 0.0))
			add(Entry(4f, 0f, 0.0))
			add(Entry(5f, 0f, 0.0))
			add(Entry(6f, 0f, 0.0))
			add(Entry(7f, 0f, 0.0))
			add(Entry(8f, 0f, 0.0))
			add(Entry(9f, 0f, 0.0))
			add(Entry(10f, 0f, 0.0))
			add(Entry(11f, 0f, 0.0))
		}
		
		val monthGroupIncomeList = incomeList.groupBy { monthFormatter.format(it.dateCreated) }
		val monthGroupExpenseList = expenseList.groupBy { monthFormatter.format(it.dateCreated) }
		
		monthGroupIncomeList.forEachMap { k, v ->
			val totalAmount = v.sumOf { it.amount }
			val entryIndex = AppUtil.shortMonths.indexOf { it.contentEquals(k, true) }
			
			incomeListEntry[entryIndex] = Entry(
				entryIndex.toFloat(),
				totalAmount.toFloat(),
				totalAmount
			)
		}
		
		monthGroupExpenseList.forEachMap { k, v ->
			val totalAmount = v.sumOf { it.amount }
			val entryIndex = AppUtil.shortMonths.indexOf { it.contentEquals(k, true) }
			
			expenseListEntry[entryIndex] = Entry(
				entryIndex.toFloat(),
				totalAmount.toFloat(),
				totalAmount
			)
		}
		
		return incomeListEntry to expenseListEntry
	}
	
}