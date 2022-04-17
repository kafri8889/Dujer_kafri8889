package com.anafthdev.dujer.ui.screen.dashboard

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.model.Currency
import com.github.mikephil.charting.data.Entry

data class DashboardState(
	val userBalance: Double = 0.0,
	val currentCurrency: Currency = Currency.INDONESIAN,
	val incomeFinancialList: List<Financial> = emptyList(),
	val expenseFinancialList: List<Financial> = emptyList(),
	val incomeLineDataSetEntry: List<Entry> = emptyList(),
	val expenseLineDataSetEntry: List<Entry> = emptyList()
)