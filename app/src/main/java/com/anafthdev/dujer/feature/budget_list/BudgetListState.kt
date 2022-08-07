package com.anafthdev.dujer.feature.budget_list

import com.anafthdev.dujer.data.model.Category

data class BudgetListState(
	val isTopSnackbarShowed: Boolean = false,
	val averagePerMonthCategory: List<Pair<Double, Category>> = emptyList()
)
