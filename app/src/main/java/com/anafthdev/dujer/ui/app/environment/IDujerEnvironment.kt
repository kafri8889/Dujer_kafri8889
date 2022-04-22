package com.anafthdev.dujer.ui.app.environment

import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDujerEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getCurrentCurrency(): Flow<Currency>
	
	fun vibrate(millis: Long)
	
}