package com.anafthdev.dujer.ui.wallet.environment

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IWalletEnvironment {

	val dispatcher: CoroutineDispatcher

	suspend fun deleteWallet(wallet: Wallet)
	
	suspend fun getTransaction(walletID: Int)
	
	fun getIncomeTransaction(): Flow<List<Financial>>
	
	fun getExpenseTransaction(): Flow<List<Financial>>
	
	fun getAllWallet(): Flow<List<Wallet>>
	
	fun getWallet(): Flow<Wallet>
	
	fun getFinancial(): Flow<Financial>
	
	fun getPieEntries(): Flow<List<PieEntry>>
	
	fun getAvailableCategory(): Flow<List<Category>>
	
	fun getSelectedFinancialType(): Flow<FinancialType>
	
	
	suspend fun setWalletID(id: Int)
	
	suspend fun setFinancialID(id: Int)
	
	suspend fun setSelectedFinancialType(type: FinancialType)
	
}