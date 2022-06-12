package com.anafthdev.dujer.ui.category_transaction

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial

data class CategoryTransactionState(
	val category: Category = Category.default,
	val financial: Financial = Financial.default
)
