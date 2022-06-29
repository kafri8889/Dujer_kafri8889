package com.anafthdev.dujer.foundation.common

import com.anafthdev.dujer.data.Locale.ARABIC_ALGERIAN
import com.anafthdev.dujer.data.Locale.ARABIC_BAHRAIN
import com.anafthdev.dujer.data.Locale.ARABIC_IRAQ
import com.anafthdev.dujer.data.Locale.ARABIC_JORDANIAN
import com.anafthdev.dujer.data.Locale.ARABIC_KUWAIT
import com.anafthdev.dujer.data.Locale.ARABIC_LIBYA
import com.anafthdev.dujer.data.Locale.ARABIC_SERBIAN
import com.anafthdev.dujer.data.Locale.ARABIC_TUNISIAN
import com.anafthdev.dujer.data.Locale.ARABIC_UNITED_ARAB_EMIRATES
import com.anafthdev.dujer.data.Locale.HINDI_INDIAN
import com.anafthdev.dujer.data.Locale.INDONESIAN
import com.anafthdev.dujer.data.Locale.PORTUGUESE_BRAZIL
import com.anafthdev.dujer.data.Locale.RUSSIAN_RUSSIA
import com.anafthdev.dujer.foundation.extension.isMinus
import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException
import java.util.*


object CurrencyFormatter {
	
	fun getSymbol(
		locale: Locale,
		currencyCode: String
	): String {
		return NumberFormat.getCurrencyInstance(getLocale(currencyCode, locale)).apply{
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
		val numberFormat = NumberFormat.getCurrencyInstance(getLocale(currencyCode, locale)).apply {
			if (currencyCode.isNotBlank()) {
				Timber.i("currrrrr: -> ${currency?.currencyCode}")
				currency = Currency.getInstance(currencyCode)
				Timber.i("pinnnnnnnn: $currencyCode -> ${currency!!.currencyCode}")
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
		val numberFormat = NumberFormat.getCurrencyInstance(getLocale(currencyCode, locale)).apply {
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

private fun getLocale(currencyCode: String, default: Locale): Locale {
	return when (currencyCode) {
		com.anafthdev.dujer.data.Currency.USD.name -> Locale.US
		com.anafthdev.dujer.data.Currency.KRW.name -> Locale.KOREA
		com.anafthdev.dujer.data.Currency.CAD.name -> Locale.CANADA
		com.anafthdev.dujer.data.Currency.CNY.name -> Locale.CHINA
		com.anafthdev.dujer.data.Currency.EUR.name -> Locale.FRANCE
		com.anafthdev.dujer.data.Currency.GBP.name -> Locale.UK
		com.anafthdev.dujer.data.Currency.JPY.name -> Locale.JAPAN
		com.anafthdev.dujer.data.Currency.TWD.name -> Locale.TAIWAN
		com.anafthdev.dujer.data.Currency.IDR.name -> INDONESIAN
		com.anafthdev.dujer.data.Currency.RUB.name -> RUSSIAN_RUSSIA
		com.anafthdev.dujer.data.Currency.INR.name -> HINDI_INDIAN
		com.anafthdev.dujer.data.Currency.BRL.name -> PORTUGUESE_BRAZIL
		com.anafthdev.dujer.data.Currency.DZD.name -> ARABIC_ALGERIAN
		com.anafthdev.dujer.data.Currency.BHD.name -> ARABIC_BAHRAIN
		com.anafthdev.dujer.data.Currency.IQD.name -> ARABIC_IRAQ
		com.anafthdev.dujer.data.Currency.JOD.name -> ARABIC_JORDANIAN
		com.anafthdev.dujer.data.Currency.KWD.name -> ARABIC_KUWAIT
		com.anafthdev.dujer.data.Currency.LYD.name -> ARABIC_LIBYA
		com.anafthdev.dujer.data.Currency.RSD.name -> ARABIC_SERBIAN
		com.anafthdev.dujer.data.Currency.TND.name -> ARABIC_TUNISIAN
		com.anafthdev.dujer.data.Currency.SAR.name -> ARABIC_SERBIAN
		com.anafthdev.dujer.data.Currency.AED.name -> ARABIC_UNITED_ARAB_EMIRATES
		else -> default
	}
}
