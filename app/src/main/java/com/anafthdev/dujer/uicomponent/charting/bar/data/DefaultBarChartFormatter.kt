package com.anafthdev.dujer.uicomponent.charting.bar.data

import com.anafthdev.dujer.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.uicomponent.charting.formatter.ChartFormatter
import com.anafthdev.dujer.uicomponent.charting.formatter.XAxisFormatter
import com.anafthdev.dujer.uicomponent.charting.formatter.YAxisFormatter

class DefaultBarChartFormatter: ChartFormatter<BarData>() {
	
	private val defaultXAxisFormatter = DefaultXAxisFormatter()
	private val defaultYAxisFormatter = DefaultYAxisFormatter()
	
	override fun formatX(x: Float): String {
		return defaultXAxisFormatter.format(x)
	}
	
	override fun formatY(y: Float): String {
		return defaultYAxisFormatter.format(y)
	}
}

class DefaultXAxisFormatter: XAxisFormatter {
	
	override fun format(x: Float): String {
		return "$x"
	}
}

class DefaultYAxisFormatter: YAxisFormatter {
	
	override fun format(y: Float): String {
		return "$y"
	}
}
