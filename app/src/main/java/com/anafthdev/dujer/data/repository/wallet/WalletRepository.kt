package com.anafthdev.dujer.data.repository.wallet

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.flow.Flow

class WalletRepository(
	private val appDatabase: AppDatabase
): IWalletRepository {
	
	override fun getAllWallet(): Flow<List<Wallet>> {
		return appDatabase.walletDAO().getAllWallet()
	}
	
	override fun getFinancialTransaction(walletID: Int, financialType: Int) {
		appDatabase.walletDAO().getFinancialTransaction(walletID, financialType)
	}
	
	override suspend fun updateWallet(vararg wallet: Wallet) {
		return appDatabase.walletDAO().update(*wallet)
	}
	
	override suspend fun deleteWallet(vararg wallet: Wallet) {
		return appDatabase.walletDAO().delete(*wallet)
	}
	
	override suspend fun insertWallet(vararg wallet: Wallet) {
		return appDatabase.walletDAO().insert(*wallet)
	}
}