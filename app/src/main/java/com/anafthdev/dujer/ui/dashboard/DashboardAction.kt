package com.anafthdev.dujer.ui.dashboard

sealed class DashboardAction {
	data class SetFinancialID(val id: Int): DashboardAction()
	data class SetFinancialAction(val action: String): DashboardAction()
}
