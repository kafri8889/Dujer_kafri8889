package com.anafthdev.dujer.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.dashboard.environment.IDashboardEnvironment
import com.anafthdev.dujer.util.AppUtil
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
	dashboardEnvironment: IDashboardEnvironment
): StatefulViewModel<DashboardState, Unit, DashboardAction, IDashboardEnvironment>(DashboardState(), dashboardEnvironment) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancialAction().collect { action ->
				setState {
					copy(
						financialAction = action
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
	
	override fun dispatch(action: DashboardAction) {
		when (action) {
			is DashboardAction.NewWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertWallet(action.wallet)
				}
			}
			is DashboardAction.SetFinancialID -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFinancialID(action.id)
				}
			}
			is DashboardAction.SetFinancialAction -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFinancialAction(action.action)
				}
			}
		}
	}
	
	fun getLineDataSetEntry(
		incomeList: List<Financial>,
		expenseList: List<Financial>
	): Pair<List<Entry>, List<Entry>> {
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