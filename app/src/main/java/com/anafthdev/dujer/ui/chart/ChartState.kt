package com.anafthdev.dujer.ui.chart

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData

data class ChartState(
	val incomeFinancialList: List<Financial> = emptyList(),
	val expenseFinancialList: List<Financial> = emptyList(),
	val incomeBarDataList: List<BarData> = emptyList(),
	val expenseBarDataList: List<BarData> = emptyList()
)
