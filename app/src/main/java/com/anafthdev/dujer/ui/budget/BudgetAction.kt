package com.anafthdev.dujer.ui.budget

sealed class BudgetAction {
	data class GetBudget(val id: Int): BudgetAction()
}
