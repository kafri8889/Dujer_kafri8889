package com.anafthdev.dujer.ui.dashboard

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.github.mikephil.charting.data.Entry

data class DashboardState(
	val userBalance: Double = 0.0,
	val financial: Financial = Financial.default,
	val financialAction: String = FinancialAction.NEW,
	val incomeFinancialList: List<Financial> = emptyList(),
	val expenseFinancialList: List<Financial> = emptyList(),
	val incomeLineDataSetEntry: List<Entry> = emptyList(),
	val expenseLineDataSetEntry: List<Entry> = emptyList()
)