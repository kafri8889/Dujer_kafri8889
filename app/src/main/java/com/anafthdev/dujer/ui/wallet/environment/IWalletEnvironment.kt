package com.anafthdev.dujer.ui.wallet.environment

import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IWalletEnvironment {

	val dispatcher: CoroutineDispatcher
	
	fun getWallet(): Flow<Wallet>
	
	fun getSortType(): Flow<SortType>
	
	fun getGroupType(): Flow<GroupType>
	
	fun getFilterDate(): Flow<Pair<Long, Long>>
	
	fun getSelectedMonth(): Flow<List<Int>>
	
	fun getTransactions(): Flow<FinancialGroupData>
	
	fun getPieEntries(): Flow<List<PieEntry>>
	
	fun getAvailableCategory(): Flow<List<Category>>
	
	fun getSelectedFinancialType(): Flow<FinancialType>
	
	
	suspend fun insertFinancial(financial: Financial)
	
	suspend fun updateWallet(wallet: Wallet)
	
	suspend fun deleteWallet(wallet: Wallet)
	
	suspend fun setWalletID(id: Int)
	
	suspend fun setSortType(sortType: SortType)
	
	suspend fun setGroupType(groupType: GroupType)
	
	suspend fun setFilterDate(date: Pair<Long, Long>)
	
	suspend fun setSelectedMonth(selectedMonth: List<Int>)
	
	suspend fun setSelectedFinancialType(type: FinancialType)
	
}