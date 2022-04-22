package com.anafthdev.dujer.ui.financial

import com.anafthdev.dujer.data.db.model.Category

data class FinancialState(
	val categories: List<Category> = emptyList()
)
