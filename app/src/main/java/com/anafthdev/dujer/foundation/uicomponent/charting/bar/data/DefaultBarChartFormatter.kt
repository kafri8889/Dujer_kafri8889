package com.anafthdev.dujer.foundation.uicomponent.charting.bar.data

import com.anafthdev.dujer.foundation.uicomponent.charting.bar.model.BarData
import com.anafthdev.dujer.foundation.uicomponent.charting.formatter.ChartFormatter
import com.anafthdev.dujer.foundation.uicomponent.charting.formatter.XAxisFormatter
import com.anafthdev.dujer.foundation.uicomponent.charting.formatter.YAxisFormatter

class DefaultBarChartFormatter: ChartFormatter<BarData>() {
	
	private val defaultXAxisFormatter = DefaultXAxisFormatter()
	private val defaultYAxisFormatter = DefaultYAxisFormatter()
	
	override fun formatX(x: Float, data: List<BarData>): String {
		return defaultXAxisFormatter.format(x, data)
	}
	
	override fun formatY(y: Float, data: List<BarData>): String {
		return defaultYAxisFormatter.format(y, data)
	}
}

class DefaultXAxisFormatter: XAxisFormatter {
	
	override fun format(x: Float, data: List<BarData>): String {
		return "$x"
	}
}

class DefaultYAxisFormatter: YAxisFormatter {
	
	override fun format(y: Float, data: List<BarData>): String {
		return "$y"
	}
}
