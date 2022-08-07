package com.anafthdev.dujer.feature.wallet

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet

sealed class WalletAction {
	data class GetWallet(val id: Int): WalletAction()
	data class SetSortType(val sortType: SortType): WalletAction()
	data class SetGroupType(val groupType: GroupType): WalletAction()
	data class SetFilterDate(val filterDate: Pair<Long, Long>): WalletAction()
	data class SetSelectedMonth(val selectedMonth: List<Int>): WalletAction()
	data class UpdateWallet(val wallet: Wallet): WalletAction()
	data class DeleteWallet(val wallet: Wallet): WalletAction()
	data class InsertFinancial(val financial: Financial): WalletAction()
	data class SetSelectedFinancialType(val type: FinancialType): WalletAction()
}
