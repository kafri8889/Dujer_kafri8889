package com.anafthdev.dujer.feature.edit_wallet_balance

import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.edit_wallet_balance.data.EditBalanceOption

sealed interface EditWalletBalanceAction {
	data class Update(val wallet: Wallet): EditWalletBalanceAction
	data class ChangeBalanceOption(val option: EditBalanceOption): EditWalletBalanceAction
	data class ChangeBalance(val amount: Double, val value: TextFieldValue): EditWalletBalanceAction
}