package com.anafthdev.dujer.ui.wallet

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.wallet.environment.IWalletEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
	walletEnvironment: IWalletEnvironment
): StatefulViewModel<WalletState, Unit, WalletAction, IWalletEnvironment>(
	WalletState(),
	walletEnvironment
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
			environment.getAllWallet().collect { wallets ->
				setState {
					copy(
						wallets = wallets.sortedBy { it.id }
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
			environment.getFinancial().collect { financial ->
				setState {
					copy(
						financial = financial
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
			is WalletAction.GetTransaction -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.getTransaction(action.id)
				}
			}
			is WalletAction.GetFinancial -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setFinancialID(action.id)
				}
			}
			is WalletAction.DeleteWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteWallet(action.wallet)
				}
			}
			is WalletAction.SetSelectedFinancialType -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.setSelectedFinancialType(action.type)
				}
			}
		}
	}
	
}