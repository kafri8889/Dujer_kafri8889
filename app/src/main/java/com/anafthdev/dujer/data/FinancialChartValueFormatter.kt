package com.anafthdev.dujer.data

import com.anafthdev.dujer.foundation.extension.addStringBefore
import com.anafthdev.dujer.foundation.extension.endsWithNumber
import com.anafthdev.dujer.foundation.extension.removeNonDigitChar
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class FinancialChartValueFormatter: ValueFormatter() {
	
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
	
	private val mMaxLength = 3
	private val mFormat: DecimalFormat = DecimalFormat("###E00")
	
	override fun getFormattedValue(value: Float): String {
		return if (value > 0) format(value.toDouble()) else ""
	}
	
	fun format(number: Double): String {
		var r: String = mFormat.format(number)
		
		val numericValue1 = Character.getNumericValue(r[r.length - 1])
		val numericValue2 = Character.getNumericValue(r[r.length - 2])
		val combined = Integer.valueOf(numericValue2.toString() + "" + numericValue1)
		val suffix = mSuffix[combined / 3]
		
		r = r.replace("E[0-9][0-9]".toRegex(), suffix)
		
		var isComma = true
		val commaLoc = r.indexOfFirst {
			isComma = it == ','
			
			isComma || it == '.'
		}
		
		r = r.removeNonDigitChar()
		
		while (r.length > mMaxLength || r.matches("[0-9]+\\.[a-z]".toRegex())) {
			r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
			println(r)
		}
		
		r += suffix
		r = r.addStringBefore(
			if (isComma) "," else ".",
			commaLoc
		)
		
		return if (!r.endsWithNumber()) r.addStringBefore(
			" ",
			if (suffix.length == 2) r.lastIndex - 1 else r.lastIndex
		) else r
	}
	
}