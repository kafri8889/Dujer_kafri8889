package com.anafthdev.dujer.feature.budget

import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.model.Budget

sealed class BudgetAction {
	data class UpdateBudget(val budget: Budget): BudgetAction()
	data class DeleteBudget(val budget: Budget): BudgetAction()
	data class SetSortType(val sortType: SortType): BudgetAction()
	data class SetGroupType(val groupType: GroupType): BudgetAction()
	data class SetFilterDate(val filterDate: Pair<Long, Long>): BudgetAction()
	data class SetSelectedMonth(val months: List<Int>): BudgetAction()
}
