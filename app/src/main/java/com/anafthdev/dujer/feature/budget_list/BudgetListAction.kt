package com.anafthdev.dujer.feature.budget_list

import com.anafthdev.dujer.data.model.Budget

sealed class BudgetListAction {
	object ShowTopSnackbar: BudgetListAction()
	object HideTopSnackbar: BudgetListAction()
	
	data class InsertBudget(val budget: Budget): BudgetListAction()
}
