package com.anafthdev.dujer.ui.financial

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.model.Currency

data class FinancialState(
	val currentCurrency: Currency = Currency.INDONESIAN,
	val categories: List<Category> = emptyList()
)
