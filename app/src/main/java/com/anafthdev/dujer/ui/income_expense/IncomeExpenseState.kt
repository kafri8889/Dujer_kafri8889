package com.anafthdev.dujer.ui.income_expense

import com.anafthdev.dujer.data.db.model.Financial
import com.github.mikephil.charting.data.Entry

data class IncomeExpenseState(
	val financial: Financial = Financial.default,
	val incomeFinancialList: List<Financial> = emptyList(),
	val expenseFinancialList: List<Financial> = emptyList(),
	val expenseLineChartEntry: List<Entry> = emptyList(),
	val incomeLineChartEntry: List<Entry> = emptyList()
)
