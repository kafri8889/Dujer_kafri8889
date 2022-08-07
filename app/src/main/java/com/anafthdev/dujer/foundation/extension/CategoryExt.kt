package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.CategoryDb

fun Category.isDefault(): Boolean = id == Category.default.id

fun Category.toCategoryDb(): CategoryDb {
	return CategoryDb(
		id = id,
		name = name,
		iconID = iconID,
		tint = tint,
		type = type,
		defaultCategory = defaultCategory
	)
}

fun Collection<Category>.toCategoryDb(): List<CategoryDb> {
	return map { category ->
		CategoryDb(
			id = category.id,
			name = category.name,
			iconID = category.iconID,
			tint = category.tint,
			type = category.type,
			defaultCategory = category.defaultCategory
		)
	}
}

fun Array<out Category>.toCategoryDb(): Array<out CategoryDb> {
	return map { category ->
		CategoryDb(
			id = category.id,
			name = category.name,
			iconID = category.iconID,
			tint = category.tint,
			type = category.type,
			defaultCategory = category.defaultCategory
		)
	}.toTypedArray()
}
