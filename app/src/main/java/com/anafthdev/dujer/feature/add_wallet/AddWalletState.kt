package com.anafthdev.dujer.feature.add_wallet

import androidx.compose.ui.text.input.TextFieldValue
import com.anafthdev.dujer.data.WalletIcons
import com.anafthdev.dujer.model.CategoryTint

data class AddWalletState(
	val name: String = "",
	val initialBalance: Double = 0.0,
	val icon: Int = WalletIcons.WALLET,
	val tint: CategoryTint = CategoryTint.tint_1,
	
	val balanceFieldValue: TextFieldValue = TextFieldValue(),
	val isSelectorWalletTintShowed: Boolean = false,
	val isSelectorWalletIconShowed: Boolean = false
)
