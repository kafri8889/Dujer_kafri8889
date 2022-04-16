package com.anafthdev.dujer.data

sealed class DujerDestination(val route: String) {
	
	object Dashboard: DujerDestination("dashboard")
	
	object IncomeExpense: DujerDestination("incomeExpense/{type}") {
		fun createRoute(type: FinancialType): String {
			return "incomeExpense/${type.ordinal}"
		}
	}
	
	object Setting: DujerDestination("setting")
	
	object Category: DujerDestination("category")
}