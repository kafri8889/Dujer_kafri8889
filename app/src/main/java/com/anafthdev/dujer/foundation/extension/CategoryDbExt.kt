package com.anafthdev.dujer.foundation.extension

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.CategoryDb

fun CategoryDb.toCategoryWithEmptyFinancial(): Category {
	return Category(
		id = id,
		name = name,
		iconID = iconID,
		tint = tint,
		type = type,
		defaultCategory = defaultCategory
	)
}
