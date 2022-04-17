package com.anafthdev.dujer.ui.app

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.app.environment.IDujerEnvironment
import com.anafthdev.dujer.ui.screen.financial.FinancialViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DujerViewModel @Inject constructor(
	dujerEnvironment: IDujerEnvironment
): StatefulViewModel<DujerState, IDujerEnvironment>(DujerState(), dujerEnvironment) {
	
	init {
		getIsFinancialSheetShowed()
		getFinancialID()
		getFinancialAction()
	}
	
	private fun getIsFinancialSheetShowed() {
		viewModelScope.launch(environment.dispatcher) {
			environment.getIsFinancialSheetShowed().collect { isShowing ->
				setState {
					copy(
						isFinancialSheetShowed = isShowing
					)
				}
			}
		}
	}
	
	private fun getFinancialID() {
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancialID().collect { id ->
				setState {
					copy(
						financialID = id
					)
				}
			}
		}
	}
	
	private fun getFinancialAction() {
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancialAction().collect { action ->
				setState {
					copy(
						financialAction = action
					)
				}
			}
		}
	}
	
	fun navigateToFinancialScreen(id: Int, action: String) {
		environment.setFinancialID(id)
		environment.setFinancialAction(action)
		showFinancialSheet()
	}
	
	fun reset() {
		environment.setFinancialID(Financial.default.id)
		environment.setFinancialAction(FinancialViewModel.FINANCIAL_ACTION_NEW)
		hideFinancialSheet()
	}
	
	fun showFinancialSheet() {
		environment.showFinancialSheet()
	}
	
	fun hideFinancialSheet() {
		environment.hideFinancialSheet()
	}
	
}