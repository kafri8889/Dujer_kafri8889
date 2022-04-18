package com.anafthdev.dujer.ui.income_expense

import com.anafthdev.dujer.data.db.model.Financial

data class IncomeExpenseState(
	val financial: Financial = Financial.default,
	val incomeFinancialList: List<Financial> = emptyList(),
	val expenseFinancialList: List<Financial> = emptyList()
)
