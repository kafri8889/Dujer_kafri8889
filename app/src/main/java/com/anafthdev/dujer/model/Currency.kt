package com.anafthdev.dujer.model

import androidx.compose.runtime.compositionLocalOf

data class Currency(
	val name: String,
	val country: String,
	val countryCode: String,
	val symbol: String
) {
	companion object {
		
		val DOLLAR = with(android.icu.util.Currency.getInstance(com.anafthdev.dujer.data.Currency.USD.name)) {
			Currency(
				name = "Dollar",
				country = displayName,
				countryCode = currencyCode,
				symbol = symbol
			)
		}
		
		val availableCurrency = arrayListOf<Currency>().apply {
			com.anafthdev.dujer.data.Currency.values().forEach { currencyID ->
				val currency = java.util.Currency.getInstance(currencyID.name)
				add(
					Currency(
						name = "",
						country = currency.displayName,
						countryCode = currency.currencyCode,
						symbol = currency.symbol
					)
				)
			}
		}.sortedBy { it.country }
		
	}
}

val LocalCurrency = compositionLocalOf { Currency.DOLLAR }
