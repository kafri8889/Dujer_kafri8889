package com.anafthdev.dujer.data

import com.anafthdev.dujer.ui.category.data.CategoryAction

sealed class DujerDestination(val route: String) {
	
	object IncomeExpense: DujerDestination("incomeExpense/{type}") {
		fun createRoute(type: FinancialType): String {
			return "incomeExpense/${type.ordinal}"
		}
	}
	
	object Category: DujerDestination("category/{action}/{id}") {
		fun createRoute(
			action: String = CategoryAction.NOTHING,
			id: Int = com.anafthdev.dujer.data.db.model.Category.default.id
		): String {
			return "category/$action/$id"
		}
	}
	
	object Dashboard: DujerDestination("dashboard")
	
	object Setting: DujerDestination("setting")
}