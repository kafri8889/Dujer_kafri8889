package com.anafthdev.dujer.feature.search

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial

data class SearchState(
	val textQuery: String = "",
	val resultFinancial: List<Financial> = emptyList(),
	val resultCategory: List<Category> = emptyList()
)
