package com.anafthdev.dujer.ui.category.environment

import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ICategoryEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getAllCategory(): Flow<List<Category>>
	
	suspend fun updateCategory(category: Category)
	
	suspend fun deleteCategory(category: Category)
	
	suspend fun insertCategory(category: Category)
}