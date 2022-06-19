package com.anafthdev.dujer.ui.budget

import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Financial
import com.github.mikephil.charting.data.BarEntry

data class BudgetState(
	val budget: Budget = Budget.defalut,
	val sortType: SortType = SortType.A_TO_Z,
	val selectedMonth: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
	val barEntries: List<BarEntry> = emptyList(),
	val transactions: List<Financial> = emptyList()
)