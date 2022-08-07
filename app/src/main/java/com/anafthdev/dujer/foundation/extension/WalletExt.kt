package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.data.model.WalletDb

fun Wallet.toWalletDb(): WalletDb {
	return WalletDb(
		id = id,
		name = name,
		initialBalance = initialBalance,
		balance = balance,
		iconID = iconID,
		tint = tint,
		defaultWallet = defaultWallet
	)
}

fun Collection<Wallet>.toWalletDb(): List<WalletDb> {
	return map { wallet ->
		WalletDb(
			id = wallet.id,
			name = wallet.name,
			initialBalance = wallet.initialBalance,
			balance = wallet.balance,
			iconID = wallet.iconID,
			tint = wallet.tint,
			defaultWallet = wallet.defaultWallet
		)
	}
}

fun Array<out Wallet>.toWalletDb(): Array<out WalletDb> {
	return map { wallet ->
		WalletDb(
			id = wallet.id,
			name = wallet.name,
			initialBalance = wallet.initialBalance,
			balance = wallet.balance,
			iconID = wallet.iconID,
			tint = wallet.tint,
			defaultWallet = wallet.defaultWallet
		)
	}.toTypedArray()
}
