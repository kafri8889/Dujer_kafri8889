package com.anafthdev.dujer.ui.statistic

sealed class StatisticAction {
	data class GetWallet(val id: Int): StatisticAction()
}
