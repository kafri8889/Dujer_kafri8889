package com.anafthdev.dujer.ui.budget

import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Budget

sealed class BudgetAction {
	data class GetBudget(val id: Int): BudgetAction()
	data class UpdateBudget(val budget: Budget): BudgetAction()
	data class DeleteBudget(val budget: Budget): BudgetAction()
	data class SetSortType(val sortType: SortType): BudgetAction()
	data class SetSelectedMonth(val months: List<Int>): BudgetAction()
}
