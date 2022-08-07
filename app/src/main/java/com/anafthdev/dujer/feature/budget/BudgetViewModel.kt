package com.anafthdev.dujer.feature.budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.ARG_BUDGET_ID
import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.feature.budget.environment.IBudgetEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
	budgetEnvironment: IBudgetEnvironment,
	savedStateHandle: SavedStateHandle
): StatefulViewModel<BudgetState, Unit, BudgetAction, IBudgetEnvironment>(
	BudgetState(),
	budgetEnvironment
) {
	
	private val budgetID = savedStateHandle.getStateFlow(ARG_BUDGET_ID, Budget.defalut.id)
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			budgetID.collect { id ->
				environment.setBudget(id)
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getBudget().collect { budget ->
				setState {
					copy(
						budget = budget
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getBarEntries().collect { entries ->
				setState {
					copy(
						barEntries = entries
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
			environment.getThisMonthExpenses().collect { expenses ->
				setState {
					copy(
						thisMonthExpenses = expenses
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getAveragePerMonthExpense().collect { expenses ->
				setState {
					copy(
						averagePerMonthExpenses = expenses
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
	
	override fun dispatch(action: BudgetAction) {
		when (action) {
			is BudgetAction.UpdateBudget -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.updateBudget(action.budget)
				}
			}
			is BudgetAction.DeleteBudget -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteBudget(action.budget)
				}
			}
			is BudgetAction.SetSortType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSortType(action.sortType)
				}
			}
			is BudgetAction.SetSelectedMonth -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSelectedMonth(action.months)
				}
			}
			is BudgetAction.SetFilterDate -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFilterDate(action.filterDate)
				}
			}
			is BudgetAction.SetGroupType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setGroupType(action.groupType)
				}
			}
		}
	}
	
}