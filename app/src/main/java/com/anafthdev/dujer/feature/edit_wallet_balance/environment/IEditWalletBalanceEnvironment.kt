package com.anafthdev.dujer.feature.edit_wallet_balance.environment

import com.anafthdev.dujer.data.model.Wallet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IEditWalletBalanceEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getWallet(id: Int): Flow<Wallet>
	
}