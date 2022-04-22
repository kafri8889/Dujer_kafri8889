package com.anafthdev.dujer.ui.app

import com.anafthdev.dujer.model.Currency

data class DujerState(
	val currentCurrency: Currency = Currency.DOLLAR
)
