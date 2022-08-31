package com.anafthdev.dujer.feature.category.environment

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ICategoryEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getCategory(): Flow<Category>
	
	suspend fun setCategory(id: Int)
	
	suspend fun updateFinancial(vararg financial: Financial)
	
	suspend fun update(vararg category: Category)
	
	suspend fun insert(vararg category: Category)
}