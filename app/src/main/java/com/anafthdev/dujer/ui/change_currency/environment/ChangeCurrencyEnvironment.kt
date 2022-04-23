package com.anafthdev.dujer.ui.change_currency.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.repository.app.AppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class ChangeCurrencyEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: AppRepository,
	private val appDatastore: AppDatastore
): IChangeCurrencyEnvironment {
	
	private val _resultCurrency = MutableLiveData(emptyList<Currency>())
	private val resultCurrency: LiveData<List<Currency>> = _resultCurrency
	
	private val availableCurrency: ArrayList<Currency> = arrayListOf()
	
	init {
		availableCurrency.addAll(
			Currency.availableCurrency
		)
	}
	
	override suspend fun getSearchedCurrency(): Flow<List<Currency>> {
		return resultCurrency.asFlow()
	}
	
	override fun searchCurrency(query: String) {
		_resultCurrency.postValue(
			availableCurrency
				.filter { it.country.contains(query, true) }
				.distinctBy { it.countryCode }
		)
	}
	
	override fun changeCurrency(currency: Currency, action: () -> Unit) {
		appDatastore.setCurrentCurrency(currency.countryCode, action)
	}
	
}