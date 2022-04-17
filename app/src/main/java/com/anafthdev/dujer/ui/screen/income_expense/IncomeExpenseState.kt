package com.anafthdev.dujer.ui.screen.income_expense

import com.anafthdev.dujer.data.db.model.Financial

data class IncomeExpenseState(
	val incomeFinancialList: List<Financial> = emptyList(),
	val expenseFinancialList: List<Financial> = emptyList()
)
