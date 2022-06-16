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
	}
	
	override fun dispatch(action: BudgetAction) {
		when (action) {
			is BudgetAction.GetBudget -> {
				viewModelScope.launch(environment.dispatcher) {
					environment
				}
			}
		}
	}
	
}