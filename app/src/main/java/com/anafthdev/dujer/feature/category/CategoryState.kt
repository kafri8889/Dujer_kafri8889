package com.anafthdev.dujer.feature.category

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.feature.category.data.CategorySwipeAction

data class CategoryState(
	val category: Category = Category.default,
	val categoryName: String = "",
	val categoryIconID: Int = Category.default.iconID,
	val selectedFinancialType: FinancialType = FinancialType.INCOME,
	val selectedFinancialTypeForEdit: FinancialType = FinancialType.INCOME,
	val swipeAction: String = CategorySwipeAction.NOTHING
)
