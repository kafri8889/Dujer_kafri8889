package com.anafthdev.dujer.ui.statistic

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.statistic.environment.IStatisticEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
	statisticEnvironment: IStatisticEnvironment
): StatefulViewModel<StatisticState, Unit, StatisticAction, IStatisticEnvironment>(
	StatisticState(),
	statisticEnvironment
) {
	
	val percentDecimalFormat = DecimalFormat("###,###,##0.0")
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getWallet().collect { wallet ->
				setState {
					copy(
						wallet = wallet
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getIncomeTransaction().collect { transaction ->
				setState {
					copy(
						incomeTransaction = transaction
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getExpenseTransaction().collect { transaction ->
				setState {
					copy(
						expenseTransaction = transaction
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getSelectedFinancialType().collect { type ->
				setState {
					copy(
						selectedFinancialType = type
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getAvailableCategory().collect { categories ->
				setState {
					copy(
						availableCategory = categories
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getPieEntry().collect { entries ->
				setState {
					copy(
						pieEntries = entries
					)
				}
			}
		}
	}
	
	override fun dispatch(action: StatisticAction) {
		when (action) {
			is StatisticAction.Get -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.getWallet(action.walletID)
				}
			}
			is StatisticAction.SetSelectedFinancialType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSelectedFinancialType(action.type)
				}
			}
			is StatisticAction.SetSelectedDate -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSelectedDate(action.date)
				}
			}
		}
	}
	
}