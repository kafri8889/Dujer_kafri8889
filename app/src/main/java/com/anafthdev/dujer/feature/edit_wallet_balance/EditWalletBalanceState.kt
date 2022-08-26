package com.anafthdev.dujer.feature.edit_wallet_balance

import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.edit_wallet_balance.data.EditBalanceOption

data class EditWalletBalanceState(
	val wallet: Wallet? = null,
	val balanceAmount: Double = 0.0,
	val balanceFieldValue: TextFieldValue = TextFieldValue(),
	val editBalanceOption: EditBalanceOption = EditBalanceOption.CHANGE_BALANCE
	
)