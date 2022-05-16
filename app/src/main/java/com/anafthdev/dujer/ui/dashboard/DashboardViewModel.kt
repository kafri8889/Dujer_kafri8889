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
): StatefulViewModel<DashboardState, Unit, DashboardAction, IDashboardEnvironment>(DashboardState(), dashboardEnvironment) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getAllWallet().collect { wallets ->
				setState {
					copy(
						wallets = wallets.sortedBy { it.id }
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getUserBalance().collect { balance ->
				setState {
					copy(
						userBalance = balance
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancialAction().collect { action ->
				setState {
					copy(
						financialAction = action
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getFinancial().collect { financial ->
				setState {
					copy(
						financial = financial
					)
				}
			}
		}
		
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
	
	override fun dispatch(action: DashboardAction) {
		when (action) {
			is DashboardAction.NewWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertWallet(action.wallet)
				}
			}
			is DashboardAction.SetFinancialID -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFinancialID(action.id)
				}
			}
			is DashboardAction.SetFinancialAction -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFinancialAction(action.action)
				}
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
	
}