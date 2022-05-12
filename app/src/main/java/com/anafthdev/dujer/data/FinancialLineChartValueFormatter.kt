package com.anafthdev.dujer.data

import com.anafthdev.dujer.foundation.extension.addStringBefore
import com.anafthdev.dujer.foundation.extension.endsWithNumber
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.DecimalFormat

class FinancialLineChartValueFormatter: IAxisValueFormatter {
	
	private val mSuffix = arrayOf(
		"", "k", "m", "b", "t"
	)
	
	private val mMaxLength = 5
	private val mFormat: DecimalFormat = DecimalFormat("###E00")
	
	override fun getFormattedValue(value: Float, axis: AxisBase?): String {
		return if (value > 0) format(value.toDouble()) else ""
		
	}
	
	private fun format(number: Double): String {
		var r: String = mFormat.format(number)
		val numericValue1 = Character.getNumericValue(r[r.length - 1])
		val numericValue2 = Character.getNumericValue(r[r.length - 2])
		val combined = Integer.valueOf(numericValue2.toString() + "" + numericValue1)
		
		r = r.replace("E[0-9][0-9]".toRegex(), mSuffix[combined / 3])
		
		while (r.length > mMaxLength || r.matches("[0-9]+\\.[a-z]".toRegex())) {
			r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
		}
		
		return if (!r.endsWithNumber()) r.addStringBefore(" ", r.lastIndex).uppercase() else r
	}
	
}