package com.anafthdev.dujer.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.dashboard.environment.IDashboardEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
	dashboardEnvironment: IDashboardEnvironment
): StatefulViewModel<DashboardState, Unit, DashboardAction, IDashboardEnvironment>(DashboardState(), dashboardEnvironment) {
	
	init {
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
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getHighestExpenseCategory().collect { category ->
				setState {
					copy(
						highestExpenseCategory = category
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getHighestExpenseCategoryAmount().collect { amount ->
				setState {
					copy(
						highestExpenseCategoryAmount = amount
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getIncomeEntry().collect { entries ->
				setState {
					copy(
						incomeEntry = entries
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getExpenseEntry().collect { entries ->
				setState {
					copy(
						expenseEntry = entries
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
			is DashboardAction.SetSortType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSortType(action.sortType)
				}
			}
			is DashboardAction.SetSelectedMonth -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSelectedMonth(action.selectedMonth)
				}
			}
			is DashboardAction.SetFilterDate -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFilterDate(action.filterDate)
				}
			}
			is DashboardAction.SetGroupType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setGroupType(action.groupType)
				}
			}
		}
	}
	
}