package com.anafthdev.dujer.ui.category

import com.anafthdev.dujer.data.db.model.Category

data class CategoryState(
	val categories: List<Category> = emptyList()
)
