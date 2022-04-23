package com.anafthdev.dujer.data

import com.anafthdev.dujer.util.AppUtil
import com.anafthdev.dujer.util.CurrencyFormatter
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

class FinancialLineChartValueFormatter(
	private val currencyCode: String
): IAxisValueFormatter {
	
	override fun getFormattedValue(value: Float, axis: AxisBase?): String {
		return if (value > 0) CurrencyFormatter.format(
			locale = AppUtil.deviceLocale,
			amount = value.toDouble(),
			useSymbol = false,
			currencyCode = currencyCode
		) else ""
		
	}
	
}