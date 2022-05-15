package com.anafthdev.dujer.ui.wallet.environment

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IWalletEnvironment {

	val dispatcher: CoroutineDispatcher

	suspend fun deleteWallet(wallet: Wallet)
	
	suspend fun getTransaction(walletID: Int)
	
	fun getIncomeTransaction(): Flow<List<Financial>>
	
	fun getExpenseTransaction(): Flow<List<Financial>>
	
	fun getAllWallet(): Flow<List<Wallet>>
	
	suspend fun getWallet(id: Int)
	
	fun getWallet(): Flow<Wallet>
	
	suspend fun getFinancial(id: Int)
	
	fun getFinancial(): Flow<Financial>
	
}