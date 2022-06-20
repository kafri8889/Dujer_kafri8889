package com.anafthdev.dujer.ui.budget

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.budget.environment.IBudgetEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
	budgetEnvironment: IBudgetEnvironment
): StatefulViewModel<BudgetState, Unit, BudgetAction, IBudgetEnvironment>(
	BudgetState(),
	budgetEnvironment
) {
	
	init {
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
			is BudgetAction.GetBudget -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setBudget(action.id)
				}
			}
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
		}
	}
	
}