package com.anafthdev.dujer.ui.dashboard.environment

import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDashboardEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getFinancial(): Flow<Financial>
	
	fun getFinancialAction(): Flow<String>
	
	fun getSortType(): Flow<SortType>
	
	fun getFilterDate(): Flow<Pair<Long, Long>>
	
	fun getSelectedMonth(): Flow<List<Int>>
	
	fun getTransactions(): Flow<List<Financial>>
	
	fun getHighestExpenseCategory(): Flow<Category>
	
	fun getHighestExpenseCategoryAmount(): Flow<Double>
	
	
	suspend fun setFinancialID(id: Int)
	
	suspend fun insertWallet(wallet: Wallet)
	
	suspend fun setSortType(sortType: SortType)
	
	suspend fun setFilterDate(date: Pair<Long, Long>)
	
	suspend fun setSelectedMonth(selectedMonth: List<Int>)
	
	
	fun setFinancialAction(action: String)
	
}