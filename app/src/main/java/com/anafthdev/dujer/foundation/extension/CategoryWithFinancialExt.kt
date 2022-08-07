package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.relation.CategoryWithFinancial

fun Collection<CategoryWithFinancial>.toCategory(): List<Category> {
	return map { categoryWithFinancial ->
		Category(
			id = categoryWithFinancial.categoryDb.id,
			name = categoryWithFinancial.categoryDb.name,
			tint = categoryWithFinancial.categoryDb.tint,
			iconID = categoryWithFinancial.categoryDb.iconID,
			type = categoryWithFinancial.categoryDb.type,
			defaultCategory = categoryWithFinancial.categoryDb.defaultCategory,
			financials = categoryWithFinancial.financialDbs.toFinancial(categoryWithFinancial.categoryDb)
		)
	}
}

fun CategoryWithFinancial.toCategory(): Category {
	return Category(
		id = categoryDb.id,
		name = categoryDb.name,
		tint = categoryDb.tint,
		iconID = categoryDb.iconID,
		type = categoryDb.type,
		defaultCategory = categoryDb.defaultCategory,
		financials = financialDbs.toFinancial(categoryDb)
	)
}
