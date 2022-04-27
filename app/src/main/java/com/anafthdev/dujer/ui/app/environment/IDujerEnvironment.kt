package com.anafthdev.dujer.ui.app.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.data.UndoType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDujerEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getAllFinancial(): Flow<List<Financial>>
	
	suspend fun getCurrentCurrency(): Flow<Currency>
	
	suspend fun getDataCanBeReturned(): Flow<UndoType>
	
	suspend fun updateFinancial(vararg financial: Financial)
	
	suspend fun deleteFinancial(vararg financial: Financial)
	
	suspend fun deleteCategory(vararg category: Category)
	
	suspend fun undoFinancial()
	
	suspend fun undoCategory()
	
	fun vibrate(millis: Long)
	
}