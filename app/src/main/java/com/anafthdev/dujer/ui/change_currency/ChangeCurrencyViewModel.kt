package com.anafthdev.dujer.ui.change_currency

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.change_currency.environment.IChangeCurrencyEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeCurrencyViewModel @Inject constructor(
	changeCurrencyEnvironment: IChangeCurrencyEnvironment
): StatefulViewModel<ChangeCurrencyState, Unit, IChangeCurrencyEnvironment>(
	ChangeCurrencyState(),
	changeCurrencyEnvironment
) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getSearchedCurrency().collect { currencies ->
				setState {
					copy(
						resultCurrency = currencies
					)
				}
			}
		}
	}
	
	fun searchCurrency(query: String) {
		viewModelScope.launch(environment.dispatcher) {
			environment.searchCurrency(query)
		}
	}
	
	fun changeCurrency(currency: Currency, action: () -> Unit = {}) {
		viewModelScope.launch(environment.dispatcher) {
			environment.changeCurrency(currency, action)
		}
	}
	
}