package com.anafthdev.dujer.ui.statistic

sealed class StatisticAction {
	data class Get(val walletID: Int): StatisticAction()
}
