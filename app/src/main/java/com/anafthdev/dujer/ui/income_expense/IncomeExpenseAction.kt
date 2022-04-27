package com.anafthdev.dujer.ui.income_expense

sealed class IncomeExpenseAction {
	data class SetFinancialID(val id: Int): IncomeExpenseAction()
}
