package com.anafthdev.dujer.ui.income_expense

import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType

sealed class IncomeExpenseAction {
	data class SetSortType(val sortType: SortType): IncomeExpenseAction()
	data class SetGroupType(val groupType: GroupType): IncomeExpenseAction()
	data class SetFilterDate(val filterDate: Pair<Long, Long>): IncomeExpenseAction()
	data class SetSelectedMonth(val selectedMonth: List<Int>): IncomeExpenseAction()
}
