package com.anafthdev.dujer.data.repository.wallet

import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.flow.Flow

interface IWalletRepository {

	fun getAllWallet(): Flow<List<Wallet>>
	
	fun getFinancialTransaction(walletID: Int, financialType: Int)
	
	suspend fun updateWallet(vararg wallet: Wallet)
	
	suspend fun deleteWallet(vararg wallet: Wallet)
	
	suspend fun insertWallet(vararg wallet: Wallet)
}