package com.anafthdev.dujer.ui.wallet

import com.anafthdev.dujer.data.db.model.Wallet

data class WalletState(
	val wallet: Wallet = Wallet.default,
	val wallets: List<Wallet> = emptyList()
)
