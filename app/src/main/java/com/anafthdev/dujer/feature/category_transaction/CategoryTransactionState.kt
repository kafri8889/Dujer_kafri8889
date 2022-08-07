package com.anafthdev.dujer.feature.category_transaction

import com.anafthdev.dujer.data.model.Category

data class CategoryTransactionState(
	val category: Category = Category.default,
	val percent: String = "0%"
)
