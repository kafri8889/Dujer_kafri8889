package com.anafthdev.dujer.feature.add_wallet

import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.model.CategoryTint

sealed interface AddWalletAction {
	data class ChangeName(val name: String): AddWalletAction
	data class ChangeInitialBalance(val balance: Double): AddWalletAction
	data class ChangeBalanceFieldValue(val value: TextFieldValue): AddWalletAction
	data class ChangeTint(val tint: CategoryTint): AddWalletAction
	data class ChangeIcon(val icon: Int): AddWalletAction
	data class SetIsSelectorWalletTintShowed(val show: Boolean): AddWalletAction
	data class SetIsSelectorWalletIconShowed(val show: Boolean): AddWalletAction
	data class Save(val wallet: Wallet): AddWalletAction
}