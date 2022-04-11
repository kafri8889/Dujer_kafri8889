package com.anafthdev.dujer.util

import android.icu.number.Precision.currency
import com.anafthdev.dujer.foundation.extension.isMinus
import java.text.NumberFormat
import java.text.ParseException
import java.util.*


object CurrencyFormatter {

	fun format(locale: Locale, amount: Double, useSymbol: Boolean = true): String {
		var firstDigitIndex = -1
		val formattedAmount = NumberFormat.getCurrencyInstance(locale).format(amount)
		
		formattedAmount.forEachIndexed { i, c ->
			if (c.isDigit() and firstDigitIndex.isMinus()) firstDigitIndex = i
		}
		
		return "${if (useSymbol) "${formattedAmount.substring(0, firstDigitIndex)} " else ""}${formattedAmount.subSequence(firstDigitIndex, formattedAmount.length)}"
	}
	
	fun parse(locale: Locale, amount: String): Double {
		return try {
			NumberFormat.getCurrencyInstance().parse(amount)?.toDouble() ?: 0.0
		} catch (e: ParseException) {
			0.0
		}
	}
}