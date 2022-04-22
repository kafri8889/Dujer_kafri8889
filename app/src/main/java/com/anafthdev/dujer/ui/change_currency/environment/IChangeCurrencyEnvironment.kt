package com.anafthdev.dujer.ui.change_currency.environment

import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IChangeCurrencyEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getSearchedCurrency(): Flow<List<Currency>>
	
	fun searchCurrency(query: String)
	
	fun changeCurrency(currency: Currency, action: () -> Unit = {})
	
}