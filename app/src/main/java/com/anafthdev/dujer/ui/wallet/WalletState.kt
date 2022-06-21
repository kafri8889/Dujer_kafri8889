package com.anafthdev.dujer.ui.wallet

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.util.AppUtil
import com.github.mikephil.charting.data.PieEntry

data class WalletState(
	val financial: Financial = Financial.default,
	val wallet: Wallet = Wallet.default,
	val sortType: SortType = SortType.A_TO_Z,
	val filterDate: Pair<Long, Long> = AppUtil.filterDateDefault,
	val selectedMonth: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
	val transactions: List<Financial> = emptyList(),
	val pieEntries: List<PieEntry> = emptyList(),
	val availableCategory: List<Category> = emptyList(),
	val selectedFinancialType: FinancialType = FinancialType.INCOME
)
