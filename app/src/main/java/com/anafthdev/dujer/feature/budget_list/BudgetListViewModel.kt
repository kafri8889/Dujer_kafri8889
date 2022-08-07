package com.anafthdev.dujer.feature.budget_list

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.feature.budget_list.environment.IBudgetListEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetListViewModel @Inject constructor(
	budgetListEnvironment: IBudgetListEnvironment
): StatefulViewModel<BudgetListState, Unit, BudgetListAction, IBudgetListEnvironment>(
	BudgetListState(),
	budgetListEnvironment
) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getAveragePerMonthCategory().collect { average ->
				setState {
					copy(
						averagePerMonthCategory = average
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.isTopSnackbarShowed().collect { showed ->
				setState {
					copy(
						isTopSnackbarShowed = showed
					)
				}
			}
		}
	}
	
	override fun dispatch(action: BudgetListAction) {
		when (action) {
			is BudgetListAction.ShowTopSnackbar -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.showTopSnackbar(true)
				}
			}
			is BudgetListAction.HideTopSnackbar -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.showTopSnackbar(false)
				}
			}
			is BudgetListAction.InsertBudget -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertBudget(action.budget)
				}
			}
		}
	}
	
}