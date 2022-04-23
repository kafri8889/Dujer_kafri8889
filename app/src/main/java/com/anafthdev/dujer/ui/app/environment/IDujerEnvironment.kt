package com.anafthdev.dujer.ui.app.environment

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDujerEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getAllFinancial(): Flow<List<Financial>>
	
	suspend fun getCurrentCurrency(): Flow<Currency>
	
	suspend fun updateFinancial(vararg financial: Financial)
	
	fun vibrate(millis: Long)
	
}