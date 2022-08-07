package com.anafthdev.dujer.feature.change_currency.environment

import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IChangeCurrencyEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getSearchedCurrency(): Flow<List<Currency>>
	
	suspend fun changeCurrency(currency: Currency)
	
	fun searchCurrency(query: String)
	
}