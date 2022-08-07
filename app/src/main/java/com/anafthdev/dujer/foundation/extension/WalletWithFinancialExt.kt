package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.data.model.relation.WalletWithFinancial

fun Collection<WalletWithFinancial>.toWallet(): List<Wallet> {
	return map { walletWithFinancial ->
		Wallet(
			id = walletWithFinancial.walletDb.id,
			name = walletWithFinancial.walletDb.name,
			initialBalance = walletWithFinancial.walletDb.initialBalance,
			balance = walletWithFinancial.walletDb.balance,
			iconID = walletWithFinancial.walletDb.iconID,
			tint = walletWithFinancial.walletDb.tint,
			defaultWallet = walletWithFinancial.walletDb.defaultWallet,
			financials = walletWithFinancial.financials.toFinancial()
		)
	}
}

fun WalletWithFinancial.toWallet(): Wallet {
	return Wallet(
		id = walletDb.id,
		name = walletDb.name,
		initialBalance = walletDb.initialBalance,
		balance = walletDb.balance,
		iconID = walletDb.iconID,
		tint = walletDb.tint,
		defaultWallet = walletDb.defaultWallet,
		financials = financials.toFinancial()
	)
}
