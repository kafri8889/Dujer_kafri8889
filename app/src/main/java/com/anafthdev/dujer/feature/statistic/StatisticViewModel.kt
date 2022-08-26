package com.anafthdev.dujer.feature.statistic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.ARG_WALLET_ID
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.statistic.environment.IStatisticEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
	statisticEnvironment: IStatisticEnvironment,
	savedStateHandle: SavedStateHandle
): StatefulViewModel<StatisticState, Unit, StatisticAction, IStatisticEnvironment>(
	StatisticState(),
	statisticEnvironment
) {
	
	private val walletID = savedStateHandle.getStateFlow(ARG_WALLET_ID, Wallet.cash.id)
	
	val percentDecimalFormat = DecimalFormat("###,###,##0.0")
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			walletID.collect { id ->
				environment.setWallet(id)
				environment.getWallet(id).collect { wallet ->
					setState {
						copy(
							wallet = wallet
						)
					}
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