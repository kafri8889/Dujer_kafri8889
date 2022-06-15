package com.anafthdev.dujer.ui.budget_list

import com.anafthdev.dujer.data.db.model.Category

data class BudgetListState(
	val averagePerMonthCategory: List<Pair<Double, Category>> = emptyList()
)
