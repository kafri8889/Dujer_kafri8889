package com.anafthdev.dujer.ui.change_currency

import com.anafthdev.dujer.model.Currency

data class ChangeCurrencyState(
	val resultCurrency: List<Currency> = emptyList()
)
