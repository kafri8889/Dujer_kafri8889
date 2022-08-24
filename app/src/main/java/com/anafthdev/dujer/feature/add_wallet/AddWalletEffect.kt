package com.anafthdev.dujer.feature.add_wallet

sealed interface AddWalletEffect {
	object BlankWalletName: AddWalletEffect
}