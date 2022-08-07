package com.anafthdev.dujer.feature.wallet

import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.foundation.common.AppUtil
import com.github.mikephil.charting.data.PieEntry

data class WalletState(
	val wallet: Wallet = Wallet.default,
	val sortType: SortType = SortType.A_TO_Z,
	val groupType: GroupType = GroupType.DEFAULT,
	val filterDate: Pair<Long, Long> = AppUtil.filterDateDefault,
	val selectedMonth: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
	val transactions: FinancialGroupData = FinancialGroupData.default,
	val pieEntries: List<PieEntry> = emptyList(),
	val availableCategory: List<Category> = emptyList(),
	val selectedFinancialType: FinancialType = FinancialType.INCOME
)
