package com.anafthdev.dujer.feature.add_wallet.environment

import com.anafthdev.dujer.data.model.Wallet
import kotlinx.coroutines.CoroutineDispatcher

interface IAddWalletEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun save(wallet: Wallet)
	
}