package com.anafthdev.dujer.feature.search.environment

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ISearchEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun search(query: String)
	
	fun getResultFinancial(): Flow<List<Financial>>
	
	fun getResultCategory(): Flow<List<Category>>
	
}