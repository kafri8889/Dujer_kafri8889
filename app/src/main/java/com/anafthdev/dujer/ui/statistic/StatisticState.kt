package com.anafthdev.dujer.ui.statistic

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet

data class StatisticState(
	val wallet: Wallet = Wallet.cash,
	val wallets: List<Wallet> = emptyList(),
	val incomeTransaction: List<Financial> = emptyList(),
	val expenseTransaction: List<Financial> = emptyList()
)
