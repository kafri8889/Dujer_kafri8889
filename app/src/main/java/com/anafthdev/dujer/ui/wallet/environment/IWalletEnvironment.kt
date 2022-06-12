package com.anafthdev.dujer.ui.wallet.environment

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IWalletEnvironment {

	val dispatcher: CoroutineDispatcher
	
	suspend fun insertFinancial(financial: Financial)

	suspend fun updateWallet(wallet: Wallet)
	
	suspend fun deleteWallet(wallet: Wallet)
	
	fun getAllWallet(): Flow<List<Wallet>>
	
	fun getWallet(): Flow<Wallet>
	
	fun getSortType(): Flow<SortType>
	
	fun getFinancial(): Flow<Financial>
	
	fun getPieEntries(): Flow<List<PieEntry>>
	
	fun getAvailableCategory(): Flow<List<Category>>
	
	fun getSelectedFinancialType(): Flow<FinancialType>
	
	
	suspend fun setWalletID(id: Int)
	
	suspend fun setFinancialID(id: Int)
	
	suspend fun setSortType(sortType: SortType)
	
	suspend fun setSelectedFinancialType(type: FinancialType)
	
}