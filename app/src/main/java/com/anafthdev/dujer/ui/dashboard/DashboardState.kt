package com.anafthdev.dujer.ui.dashboard

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.github.mikephil.charting.data.Entry

data class DashboardState(
	val financial: Financial = Financial.default,
	val financialAction: String = FinancialAction.NEW,
)