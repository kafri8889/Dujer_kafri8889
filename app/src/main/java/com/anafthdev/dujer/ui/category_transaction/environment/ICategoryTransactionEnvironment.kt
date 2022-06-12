package com.anafthdev.dujer.ui.category_transaction.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ICategoryTransactionEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getCategory(): Flow<Category>
	
	fun getFinancial(): Flow<Financial>
	
	
	suspend fun setCategory(id: Int)
	
	suspend fun setFinancial(id: Int)
}