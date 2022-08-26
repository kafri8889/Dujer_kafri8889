package com.anafthdev.dujer.feature.edit_wallet_balance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.ARG_WALLET_ID
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.edit_wallet_balance.environment.IEditWalletBalanceEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditWalletBalanceViewModel @Inject constructor(
	editWalletBalanceEnvironment: IEditWalletBalanceEnvironment,
	savedStateHandle: SavedStateHandle
): StatefulViewModel<EditWalletBalanceState, Unit, EditWalletBalanceAction, IEditWalletBalanceEnvironment>(
	EditWalletBalanceState(),
	editWalletBalanceEnvironment
) {
	
	private val walletID = savedStateHandle.getStateFlow(ARG_WALLET_ID, Wallet.cash.id)
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			walletID.collect { id ->
				environment.getWallet(id).collect { wallet ->
					setState {
						copy(
							wallet = wallet,
							balanceAmount = wallet.initialBalance
						)
					}
				}
			}
		}
	}
	
	override fun dispatch(action: EditWalletBalanceAction) {
		when (action) {
			is EditWalletBalanceAction.Update -> {
				viewModelScope.launch(environment.dispatcher) {
				
				}
			}
			is EditWalletBalanceAction.ChangeBalance -> {
				viewModelScope.launch {
					setState {
						copy(
							balanceAmount = action.amount,
							balanceFieldValue = action.value
						)
					}
				}
			}
			is EditWalletBalanceAction.ChangeBalanceOption -> {
				viewModelScope.launch {
					setState {
						copy(
							editBalanceOption = action.option
						)
					}
				}
			}
		}
	}
}