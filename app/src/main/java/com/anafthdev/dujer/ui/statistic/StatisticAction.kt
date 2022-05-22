package com.anafthdev.dujer.ui.statistic

import com.anafthdev.dujer.data.FinancialType

sealed class StatisticAction {
	data class Get(val walletID: Int): StatisticAction()
	data class SetSelectedFinancialType(val type: FinancialType): StatisticAction()
	data class SetSelectedDate(val date: Long): StatisticAction()
}
