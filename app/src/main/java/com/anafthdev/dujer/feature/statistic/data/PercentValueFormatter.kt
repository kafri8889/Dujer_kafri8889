package com.anafthdev.dujer.feature.statistic.data

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class PercentValueFormatter: ValueFormatter() {
	
	private val mFormat = DecimalFormat("###,###,##0.0")
	
	override fun getFormattedValue(value: Float): String {
		return "${mFormat.format(value)} %"
	}
}
