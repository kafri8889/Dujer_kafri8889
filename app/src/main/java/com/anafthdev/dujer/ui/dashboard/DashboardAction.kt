package com.anafthdev.dujer.ui.dashboard

import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Wallet

sealed class DashboardAction {
	data class NewWallet(val wallet: Wallet): DashboardAction()
	data class SetSortType(val sortType: SortType): DashboardAction()
	data class SetGroupType(val groupType: GroupType): DashboardAction()
	data class SetFilterDate(val filterDate: Pair<Long, Long>): DashboardAction()
	data class SetSelectedMonth(val selectedMonth: List<Int>): DashboardAction()
}
