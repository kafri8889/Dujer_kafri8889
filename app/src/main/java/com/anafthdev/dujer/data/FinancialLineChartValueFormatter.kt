package com.anafthdev.dujer.data

import com.anafthdev.dujer.foundation.extension.addStringBefore
import com.anafthdev.dujer.foundation.extension.endsWithNumber
import com.anafthdev.dujer.foundation.extension.takeNonDigitString
import com.github.mikephil.charting.formatter.ValueFormatter
import timber.log.Timber
import java.text.DecimalFormat

class FinancialLineChartValueFormatter: ValueFormatter() {
	
	private val mSuffix = arrayOf(
		"",
		"k",
		"m",
		"b",
		"t",
		"aa",
		"ab",
		"ac",
		"ad",
		"ae",
		"af",
		"ag",
		"ah",
		"ai",
		"aj",
		"ak",
		"al",
		"am",
		"an",
		"ao",
		"ap",
		"aq",
		"ar",
		"as",
		"at",
		"au",
		"av",
		"aw",
		"ax",
		"ay",
		"az",
	)
	
	private val mFormat: DecimalFormat = DecimalFormat("###E00")
	
	override fun getFormattedValue(value: Float): String {
		return if (value > 0) format(value.toDouble()) else ""
	}
	
	private fun format(number: Double): String {
		var r: String = mFormat.format(number)
		val numericValue1 = Character.getNumericValue(r[r.length - 1])
		val numericValue2 = Character.getNumericValue(r[r.length - 2])
		val combined = Integer.valueOf(numericValue2.toString() + "" + numericValue1)
		
		r = r.replace("E[0-9][0-9]".toRegex(), mSuffix[combined / 3])
		r = r.substring(0, r.length - mSuffix[combined / 3].length)
		r = r.reversed().substring(combined).reversed().replace(".", "") + mSuffix[combined/3]
		
		return r
	}
	
}