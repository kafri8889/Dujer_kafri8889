package com.anafthdev.dujer.ui.dashboard

import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.ui.dashboard.environment.DashboardEnvironment
import com.github.mikephil.charting.data.Entry

data class DashboardState(
	val sortType: SortType = SortType.A_TO_Z,
	val groupType: GroupType = GroupType.DEFAULT,
	val filterDate: Pair<Long, Long> = AppUtil.filterDateDefault,
	val selectedMonth: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
	val transactions: FinancialGroupData = FinancialGroupData.default,
	val highestExpenseCategory: Category = Category.default,
	val highestExpenseCategoryAmount: Double = 0.0,
	val incomeEntry: List<Entry> = DashboardEnvironment.entryTemp,
	val expenseEntry: List<Entry> = DashboardEnvironment.entryTemp
)