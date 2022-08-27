package com.anafthdev.dujer.feature.wallet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.ARG_WALLET_ID
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.wallet.environment.IWalletEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
	walletEnvironment: IWalletEnvironment,
	savedStateHandle: SavedStateHandle
): StatefulViewModel<WalletState, Unit, WalletAction, IWalletEnvironment>(
	WalletState(),
	walletEnvironment
) {
	
	private val walletID = savedStateHandle.getStateFlow(ARG_WALLET_ID, Wallet.cash.id)
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			walletID.collect { id ->
				environment.setWalletID(id)
			}
		}
		
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
			environment.getSelectedFinancialType().collect { type ->
				setState {
					copy(
						selectedFinancialType = type
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getTransactions().collect { transactions ->
				setState {
					copy(
						transactions = transactions
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getSortType().collect { type ->
				setState {
					copy(
						sortType = type
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getGroupType().collect { type ->
				setState {
					copy(
						groupType = type
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getFilterDate().collect { date ->
				setState {
					copy(
						filterDate = date
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getSelectedMonth().collect { months ->
				setState {
					copy(
						selectedMonth = months
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
			environment.getPieEntries().collect { entries ->
				setState {
					copy(
						pieEntries = entries
					)
				}
			}
		}
	}
	
	override fun dispatch(action: WalletAction) {
		when (action) {
			is WalletAction.GetWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setWalletID(action.id)
				}
			}
			is WalletAction.UpdateWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.updateWallet(action.wallet)
				}
			}
			is WalletAction.InsertFinancial -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertFinancial(action.financial)
				}
			}
			is WalletAction.SetSelectedFinancialType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSelectedFinancialType(action.type)
				}
			}
			is WalletAction.SetSortType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSortType(action.sortType)
				}
			}
			is WalletAction.SetSelectedMonth -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSelectedMonth(action.selectedMonth)
				}
			}
			is WalletAction.SetFilterDate -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFilterDate(action.filterDate)
				}
			}
			is WalletAction.SetGroupType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setGroupType(action.groupType)
				}
			}
		}
	}
	
}