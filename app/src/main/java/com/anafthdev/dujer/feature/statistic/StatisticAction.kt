package com.anafthdev.dujer.feature.statistic

import com.anafthdev.dujer.data.FinancialType

sealed class StatisticAction {
	data class SetSelectedFinancialType(val type: FinancialType): StatisticAction()
	data class SetSelectedDate(val date: Long): StatisticAction()
}
