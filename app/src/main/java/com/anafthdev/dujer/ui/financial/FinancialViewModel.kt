package com.anafthdev.dujer.ui.financial

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.financial.environment.IFinancialEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancialViewModel @Inject constructor(
	financialEnvironment: IFinancialEnvironment
): StatefulViewModel<FinancialState, Unit, FinancialAction, IFinancialEnvironment>(FinancialState, financialEnvironment) {
	
	override fun dispatch(action: FinancialAction) {
		when (action) {
			is FinancialAction.Update -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.updateFinancial(action.financial)
				}
			}
			is FinancialAction.Insert -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertFinancial(action.financial)
				}
			}
		}
	}
	
}