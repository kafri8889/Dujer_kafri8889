package com.anafthdev.dujer.ui.chart.data

import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.percentOf
import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.uicomponent.charting.formatter.ChartFormatter
import com.anafthdev.dujer.util.AppUtil
import java.math.RoundingMode
import java.text.DecimalFormat

class MonthBarChartFormatter: ChartFormatter<BarData>() {
	
	override fun formatX(x: Float, data: List<BarData>): String {
		return AppUtil.shortMonths[x.toInt()]
	}
	
	override fun formatY(y: Float, data: List<BarData>): String {
		val percent = data.getBy { it.y }
			.sum()
			.percentOf(y)
		
		return "${
			DecimalFormat("#").apply {
				roundingMode = if ((percent - percent.toInt()) >= 0.5f) RoundingMode.CEILING
				else RoundingMode.FLOOR
			}.format(percent)
		}%"
	}
}