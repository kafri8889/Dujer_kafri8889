package com.anafthdev.dujer.ui.change_currency

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.change_currency.environment.IChangeCurrencyEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeCurrencyViewModel @Inject constructor(
	changeCurrencyEnvironment: IChangeCurrencyEnvironment
): StatefulViewModel<ChangeCurrencyState, Unit, ChangeCurrencyAction, IChangeCurrencyEnvironment>(
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
	
	override fun dispatch(action: ChangeCurrencyAction) {
		when (action) {
			is ChangeCurrencyAction.Search -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.searchCurrency(action.query)
				}
			}
			is ChangeCurrencyAction.ChangeCurrency -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.changeCurrency(action.currency)
				}
			}
		}
	}
	
}