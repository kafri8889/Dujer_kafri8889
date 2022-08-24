package com.anafthdev.dujer.feature.add_wallet

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.feature.add_wallet.environment.IAddWalletEnvironment
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWalletViewModel @Inject constructor(
	addWalletEnvironment: IAddWalletEnvironment
): StatefulViewModel<AddWalletState, AddWalletEffect, AddWalletAction, IAddWalletEnvironment>(
	AddWalletState(),
	addWalletEnvironment
) {
	
	init {
	
	}
	
	override fun dispatch(action: AddWalletAction) {
		when (action) {
			is AddWalletAction.ChangeName -> {
				viewModelScope.launch {
					setState {
						copy(
							name = action.name
						)
					}
				}
			}
			is AddWalletAction.ChangeInitialBalance -> {
				viewModelScope.launch {
					setState {
						copy(
							initialBalance = action.balance
						)
					}
				}
			}
			is AddWalletAction.ChangeBalanceFieldValue -> {
				viewModelScope.launch {
					setState {
						copy(
							balanceFieldValue = action.value
						)
					}
				}
			}
			is AddWalletAction.ChangeTint -> {
				viewModelScope.launch {
					setState {
						copy(
							tint = action.tint
						)
					}
				}
			}
			is AddWalletAction.ChangeIcon -> {
				viewModelScope.launch {
					setState {
						copy(
							icon = action.icon
						)
					}
				}
			}
			is AddWalletAction.SetIsSelectorWalletTintShowed -> {
				viewModelScope.launch {
					setState {
						copy(
							isSelectorWalletTintShowed = action.show
						)
					}
				}
			}
			is AddWalletAction.SetIsSelectorWalletIconShowed -> {
				viewModelScope.launch {
					setState {
						copy(
							isSelectorWalletIconShowed = action.show
						)
					}
				}
			}
			is AddWalletAction.Save -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.save(action.wallet)
				}
			}
		}
	}
	
	fun validateWalletName(name: String): Boolean {
		return when {
			name.isBlank() -> {
				setEffect(AddWalletEffect.BlankWalletName)
				
				false
			}
			else -> true
		}
	}
}