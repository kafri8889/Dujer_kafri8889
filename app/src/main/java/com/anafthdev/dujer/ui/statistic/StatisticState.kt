package com.anafthdev.dujer.ui.statistic

import com.anafthdev.dujer.data.db.model.Wallet

data class StatisticState(
	val wallet: Wallet = Wallet.cash
)
