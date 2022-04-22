package com.anafthdev.dujer.util

import com.anafthdev.dujer.foundation.extension.isMinus
import java.text.NumberFormat
import java.text.ParseException
import java.util.*


object CurrencyFormatter {
	
	fun getSymbol(
		locale: Locale,
		currencyCode: String
	): String {
		return NumberFormat.getCurrencyInstance(locale).apply{
			currency = Currency.getInstance(currencyCode)
		}.format(1.0).replace("[0-9.,]".toRegex(), "")
	}

	fun format(
		locale: Locale,
		amount: Double,
		useSymbol: Boolean = true,
		currencyCode: String = ""
	): String {
		var firstDigitIndex = -1
		val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
			if (currencyCode.isNotBlank()) {
				currency = Currency.getInstance(currencyCode)
			}
		}
		
		val formattedAmount = numberFormat.format(amount)
		
		formattedAmount.forEachIndexed { i, c ->
			if (c.isDigit() and firstDigitIndex.isMinus()) firstDigitIndex = i
		}
		
		return "${if (useSymbol) "${formattedAmount.substring(0, firstDigitIndex)} " else ""}${formattedAmount.subSequence(firstDigitIndex, formattedAmount.length)}"
	}
	
	fun parse(
		locale: Locale,
		amount: String,
		currencyCode: String = ""
	): Double {
		val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
			if (currencyCode.isNotBlank()) {
				currency = Currency.getInstance(currencyCode)
			}
		}
		
		return try {
			numberFormat.parse(amount)?.toDouble() ?: 0.0
		} catch (e: ParseException) {
			0.0
		}
	}
}