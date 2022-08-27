package com.anafthdev.dujer.feature.edit_wallet_balance

import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.edit_wallet_balance.data.EditBalanceOption

sealed interface EditWalletBalanceAction {
	data class Update(val wallet: Wallet, val financial: Financial?): EditWalletBalanceAction
	data class ChangeBalanceOption(val option: EditBalanceOption): EditWalletBalanceAction
	data class ChangeBalance(val amount: Double, val value: TextFieldValue): EditWalletBalanceAction
}