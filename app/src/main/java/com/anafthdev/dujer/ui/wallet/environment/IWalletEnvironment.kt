package com.anafthdev.dujer.ui.wallet.environment

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IWalletEnvironment {

	val dispatcher: CoroutineDispatcher

	suspend fun deleteWallet(wallet: Wallet)
	
	suspend fun getIncomeTransaction(walletID: Int)
	
	suspend fun getExpenseTransaction(walletID: Int)
	
	fun getIncomeTransaction(): Flow<List<Financial>>
	
	fun getExpenseTransaction(): Flow<List<Financial>>
	
	fun getAllWallet(): Flow<List<Wallet>>
	
	fun getWallet(id: Int)
	
	fun getWallet(): Flow<Wallet>
	
}