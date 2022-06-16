package com.anafthdev.dujer.ui.budget_list

import com.anafthdev.dujer.data.db.model.Budget

sealed class BudgetListAction {
	object ShowTopSnackbar: BudgetListAction()
	object HideTopSnackbar: BudgetListAction()
	
	data class InsertBudget(val budget: Budget): BudgetListAction()
}
