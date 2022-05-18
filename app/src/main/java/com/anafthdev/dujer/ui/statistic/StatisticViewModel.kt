package com.anafthdev.dujer.ui.statistic

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.statistic.environment.IStatisticEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
	statisticEnvironment: IStatisticEnvironment
): StatefulViewModel<StatisticState, Unit, StatisticAction, IStatisticEnvironment>(
	StatisticState(),
	statisticEnvironment
) {
	
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
	}
	
	override fun dispatch(action: StatisticAction) {
		when (action) {
			is StatisticAction.Get -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.getWallet(action.walletID)
				}
			}
		}
	}
	
}