package com.anafthdev.dujer.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.dashboard.environment.IDashboardEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
	dashboardEnvironment: IDashboardEnvironment
): StatefulViewModel<DashboardState, IDashboardEnvironment>(DashboardState(), dashboardEnvironment) {
	
	init {
		getUserBalanceAndCurrentCurrency()
		getFinancialListAndCalculateEntry()
		getFinancialID()
		getFinancialAction()
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
	
	private fun getFinancialID() {
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancial().collect { financial ->
				setState {
					copy(
						financial = financial
					)
				}
			}
		}
	}
	
	private fun getUserBalanceAndCurrentCurrency() {
		viewModelScope.launch(environment.dispatcher) {
			environment.getUserBalance()
				.combine(environment.getCurrentCurrency()) { userBalance, currentCurrency ->
					userBalance to currentCurrency
				}.collect { pair ->
					setState {
						copy(
							userBalance = pair.first,
							currentCurrency = pair.second
						)
					}
				}
		}
	}
	
	private fun getFinancialListAndCalculateEntry() {
		viewModelScope.launch(environment.dispatcher) {
			environment.getIncomeFinancialList()
				.combine(environment.getExpenseFinancialList()) { income, expense ->
					income to expense
				}.collect { pair ->
					setState {
						copy(
							incomeFinancialList = pair.first,
							expenseFinancialList = pair.second
						)
					}
					
					calculateIncomeExpenseLineDatasetEntry(
						incomeList = pair.first,
						expenseList = pair.second
					)
				}
		}
	}
	
	private fun calculateIncomeExpenseLineDatasetEntry(
		incomeList: List<Financial>,
		expenseList: List<Financial>
	) {
		viewModelScope.launch(environment.dispatcher) {
			val datasetEntry = environment.getLineDataSetEntry(
				incomeList = incomeList,
				expenseList = expenseList
			)
			
			setState {
				copy(
					incomeLineDataSetEntry = datasetEntry.first,
					expenseLineDataSetEntry = datasetEntry.second
				)
			}
		}
	}
	
	fun deleteRecord(financial: Financial) {
		viewModelScope.launch {
			environment.deleteRecord(financial)
		}
	}
	
	fun setFinancialID(id: Int) {
		viewModelScope.launch(environment.dispatcher) {
			environment.setFinancialID(id)
		}
	}
	
	fun setFinancialAction(action: String) {
		environment.setFinancialAction(action)
	}
}