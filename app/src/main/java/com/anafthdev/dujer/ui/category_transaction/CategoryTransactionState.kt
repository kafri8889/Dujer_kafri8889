package com.anafthdev.dujer.ui.category_transaction

import com.anafthdev.dujer.data.db.model.Category

data class CategoryTransactionState(
	val category: Category = Category.default
)
