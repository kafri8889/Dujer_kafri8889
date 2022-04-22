package com.anafthdev.dujer.ui.search.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface ISearchEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun search(query: String)
	
	fun getResultFinancial(): Flow<List<Financial>>
	
	fun getResultCategory(): Flow<List<Category>>
	
}