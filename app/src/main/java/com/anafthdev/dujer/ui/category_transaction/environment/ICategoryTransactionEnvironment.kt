package com.anafthdev.dujer.ui.category_transaction.environment

import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ICategoryTransactionEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getCategory(): Flow<Category>
	
	
	suspend fun setCategory(id: Int)
}