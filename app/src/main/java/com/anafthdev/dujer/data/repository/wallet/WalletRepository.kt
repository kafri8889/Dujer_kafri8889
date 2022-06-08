package com.anafthdev.dujer.data.repository.wallet

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class WalletRepository(
	private val appDatabase: AppDatabase
) {
	
	fun getAllWallet(): Flow<List<Wallet>> {
		return appDatabase.walletDAO().getAllWallet().distinctUntilChanged()
	}
	
	fun get(mID: Int): Wallet? {
		return appDatabase.walletDAO().get(mID)
	}
	
	suspend fun updateWallet(vararg wallet: Wallet) {
		return appDatabase.walletDAO().update(*wallet)
	}
	
	suspend fun deleteWallet(vararg wallet: Wallet) {
		return appDatabase.walletDAO().delete(*wallet)
	}
	
	fun insertWallet(vararg wallet: Wallet) {
		return appDatabase.walletDAO().insert(*wallet)
	}
}