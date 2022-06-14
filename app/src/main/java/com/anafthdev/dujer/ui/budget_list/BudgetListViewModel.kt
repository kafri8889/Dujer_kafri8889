package com.anafthdev.dujer.ui.budget_list

import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.budget_list.environment.IBudgetListEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BudgetListViewModel @Inject constructor(
	budgetListEnvironment: IBudgetListEnvironment
): StatefulViewModel<BudgetListState, Unit, BudgetListAction, IBudgetListEnvironment>(
	BudgetListState,
	budgetListEnvironment
) {
	
	override fun dispatch(action: BudgetListAction) {
		when (action) {
		
		}
	}
	
}