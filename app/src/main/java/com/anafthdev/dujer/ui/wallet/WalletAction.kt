package com.anafthdev.dujer.ui.wallet

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet

sealed class WalletAction {
	data class GetWallet(val id: Int): WalletAction()
	data class GetFinancial(val id: Int): WalletAction()
	data class UpdateWallet(val wallet: Wallet): WalletAction()
	data class DeleteWallet(val wallet: Wallet): WalletAction()
	data class InsertFinancial(val financial: Financial): WalletAction()
	data class SetSelectedFinancialType(val type: FinancialType): WalletAction()
}
