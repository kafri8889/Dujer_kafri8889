package com.anafthdev.dujer.ui.wallet

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet

data class WalletState(
	val wallet: Wallet = Wallet.default,
	val wallets: List<Wallet> = emptyList(),
	val incomeTransaction: List<Financial> = emptyList(),
	val expenseTransaction: List<Financial> = emptyList()
)
