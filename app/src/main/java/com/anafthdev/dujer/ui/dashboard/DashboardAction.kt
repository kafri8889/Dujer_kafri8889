package com.anafthdev.dujer.ui.dashboard

import com.anafthdev.dujer.data.db.model.Wallet

sealed class DashboardAction {
	data class NewWallet(val wallet: Wallet): DashboardAction()
	data class SetFinancialID(val id: Int): DashboardAction()
	data class SetFinancialAction(val action: String): DashboardAction()
}
