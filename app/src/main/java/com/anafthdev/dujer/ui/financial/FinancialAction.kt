package com.anafthdev.dujer.ui.financial

import com.anafthdev.dujer.data.db.model.Financial

sealed class FinancialAction {
	data class Update(val financial: Financial): FinancialAction()
	data class Insert(val financial: Financial): FinancialAction()
}
