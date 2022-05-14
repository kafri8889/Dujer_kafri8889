package com.anafthdev.dujer.util

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.replace
import com.anafthdev.dujer.foundation.extension.replaceFirstChar
import com.anafthdev.dujer.foundation.extension.startsWith
import timber.log.Timber

object TextFieldCurrencyFormatter {
	
	fun getFormattedCurrency(
		fieldValue: TextFieldValue,
		countryCode: String
	): Pair<Double, TextFieldValue> {
		val financialAmountDouble: Double
		val financialAmount: TextFieldValue
		
		var selectionIndex = fieldValue.selection
		var amount = fieldValue.text.replace(
			oldValue = listOf("-", " "),
			newValue = "",
			ignoreCase = true
		)
		
		if (amount != fieldValue.text) {
			selectionIndex = TextRange(selectionIndex.start - 1)
		}
		
		while (amount.startsWith(listOf(",", "."))) {
			amount = amount.replaceFirstChar("")
			selectionIndex = TextRange.Zero
		}
		
		financialAmountDouble = CurrencyFormatter.parse(
			locale = deviceLocale,
			amount = "${CurrencyFormatter.getSymbol(deviceLocale, countryCode)}$amount",
			currencyCode = countryCode,
		)

		Timber.i("amont: $financialAmountDouble")
		Timber.i("amont s: $amount")
		
		financialAmount = fieldValue.copy(
			text = CurrencyFormatter.format(
				locale = deviceLocale,
				amount = financialAmountDouble,
				useSymbol = false,
				currencyCode = countryCode
			),
			selection = selectionIndex
		)
		
		Timber.i("financial amont: ${financialAmount.text}")
		
		return financialAmountDouble to financialAmount
	}
	
}