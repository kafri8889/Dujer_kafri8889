package com.anafthdev.dujer.ui.app

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.applyElement
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.data.UndoType
import com.anafthdev.dujer.ui.app.environment.IDujerEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DujerViewModel @Inject constructor(
	dujerEnvironment: IDujerEnvironment
): StatefulViewModel<DujerState, DujerEffect, DujerAction, IDujerEnvironment>(DujerState(), dujerEnvironment) {
	
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
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getDataCanBeReturned().collect { undoType ->
				setState {
					copy(
						dataCanReturned = undoType
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
	
	override fun dispatch(action: DujerAction) {
		when (action) {
			is DujerAction.Undo -> {
				viewModelScope.launch(environment.dispatcher) {
					if (action.type == UndoType.Financial) {
						environment.undoFinancial()
					} else environment.undoCategory()
				}
			}
			is DujerAction.Vibrate -> {
				viewModelScope.launch {
					environment.vibrate(action.durationInMillis)
				}
			}
			is DujerAction.DeleteFinancial -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteFinancial(*action.financials)
					withContext(Dispatchers.Main) {
						setEffect(
							DujerEffect.DeleteFinancial(action.financials[0])
						)
					}
				}
			}
			is DujerAction.DeleteCategory -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteCategory(*action.categories)
					withContext(Dispatchers.Main) {
						setEffect(
							DujerEffect.DeleteCategory(action.categories[0])
						)
					}
				}
			}
		}
	}
	
}