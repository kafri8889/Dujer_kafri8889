package com.anafthdev.dujer.ui.app

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.applyElement
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.environment.IDujerEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DujerViewModel @Inject constructor(
	dujerEnvironment: IDujerEnvironment
): StatefulViewModel<DujerState, Unit, IDujerEnvironment>(DujerState(), dujerEnvironment) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getCurrentCurrency().collect { currency ->
				val financialList = environment.getAllFinancial().first()
				
				changeFinancialCurrency(financialList, currency)
				
				setState {
					copy(
						currentCurrency = currency
					)
				}
			}
		}
	}
	
	private suspend fun changeFinancialCurrency(financialList: List<Financial>, currency: Currency) {
		environment.updateFinancial(
			*financialList
				.filter { it.currency.countryCode != currency.countryCode }
				.applyElement { it.copy(currency = currency) }
				.toTypedArray()
		)
	}
	
	fun vibrate(millis: Long) {
		viewModelScope.launch {
			environment.vibrate(millis)
		}
	}
	
}