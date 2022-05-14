package com.anafthdev.dujer.ui.wallet

import com.anafthdev.dujer.data.db.model.Wallet

sealed class WalletAction {
	data class GetWallet(val id: Int): WalletAction()
	data class DeleteWallet(val wallet: Wallet): WalletAction()
}
