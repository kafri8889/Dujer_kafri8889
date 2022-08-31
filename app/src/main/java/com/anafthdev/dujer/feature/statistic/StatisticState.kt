package com.anafthdev.dujer.feature.statistic

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.github.mikephil.charting.data.PieEntry

data class StatisticState(
	val wallet: Wallet = Wallet.cash,
	val wallets: List<Wallet> = emptyList(),
	val pieEntries: List<PieEntry> = emptyList(),
	val availableCategory: List<Category> = emptyList(),
	val incomeTransaction: List<Financial> = emptyList(),
	val expenseTransaction: List<Financial> = emptyList(),
	val selectedFinancialType: FinancialType = FinancialType.INCOME
)
