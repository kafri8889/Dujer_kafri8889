package com.anafthdev.dujer.ui.app

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.ui.screen.financial.FinancialViewModel

data class DujerState(
	val isFinancialSheetShowed: Boolean = false,
	val financialID: Int = Financial.default.id,
	val financialAction: String = FinancialViewModel.FINANCIAL_ACTION_NEW
)
