package com.anafthdev.dujer.ui.chart

sealed class ChartAction {
	data class GetData(val yearInMillis: Long): ChartAction()
}
