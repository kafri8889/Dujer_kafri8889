package com.anafthdev.dujer.feature.add_wallet

import com.anafthdev.dujer.foundation.common.BaseEffect

sealed interface AddWalletEffect {
	object BlankWalletName: AddWalletEffect, BaseEffect
	object WalletCreated: AddWalletEffect, BaseEffect
}