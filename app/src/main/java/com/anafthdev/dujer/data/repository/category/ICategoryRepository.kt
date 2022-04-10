package com.anafthdev.dujer.data.repository.category

import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
	
	suspend fun getAllCategory(): Flow<List<Category>>
	
	suspend fun get(mID: Int): Category
	
	suspend fun update(vararg category: Category)
	
	suspend fun delete(vararg category: Category)
	
	suspend fun insert(vararg category: Category)
	
}