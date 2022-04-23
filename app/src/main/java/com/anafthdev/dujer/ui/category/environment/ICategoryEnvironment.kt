package com.anafthdev.dujer.ui.category.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ICategoryEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getAllFinancial(): Flow<List<Financial>>
	
	suspend fun getAll(): Flow<List<Category>>
	
	suspend fun get(id: Int, action: (Category) -> Unit)
	
	suspend fun updateFinancial(vararg financial: Financial)
	
	suspend fun update(category: Category)
	
	suspend fun delete(category: Category)
	
	suspend fun insert(category: Category)
}