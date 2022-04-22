package com.anafthdev.dujer.ui.search

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial

data class SearchState(
	val textQuery: String = "",
	val resultFinancial: List<Financial> = emptyList(),
	val resultCategory: List<Category> = emptyList()
)
