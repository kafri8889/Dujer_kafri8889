package com.anafthdev.dujer.ui.budget_list

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.budget_list.environment.IBudgetListEnvironment
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
	}
	
	override fun dispatch(action: BudgetListAction) {
		when (action) {
		
		}
	}
	
}