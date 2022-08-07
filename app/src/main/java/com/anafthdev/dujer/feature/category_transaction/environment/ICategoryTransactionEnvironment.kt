package com.anafthdev.dujer.feature.category_transaction.environment

import com.anafthdev.dujer.data.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ICategoryTransactionEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getCategory(): Flow<Category>
	
	fun getPercent(): Flow<Double>
	
	
	suspend fun setCategory(id: Int)
}