package com.anafthdev.dujer.ui.dashboard

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.ui.financial.data.FinancialAction

data class DashboardState(
	val financial: Financial = Financial.default,
	val financialAction: String = FinancialAction.NEW,
	val highestExpenseCategory: Category = Category.default,
	val highestExpenseCategoryAmount: Double = 0.0
)