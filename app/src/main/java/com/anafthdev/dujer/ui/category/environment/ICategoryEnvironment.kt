package com.anafthdev.dujer.ui.category.environment

import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ICategoryEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun get(id: Int, action: (Category) -> Unit)
	
	suspend fun getAll(): Flow<List<Category>>
	
	suspend fun update(category: Category)
	
	suspend fun delete(category: Category)
	
	suspend fun insert(category: Category)
}