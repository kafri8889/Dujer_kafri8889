package com.anafthdev.dujer.ui.dashboard.environment

import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Wallet
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDashboardEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getSortType(): Flow<SortType>
	
	fun getGroupType(): Flow<GroupType>
	
	fun getFilterDate(): Flow<Pair<Long, Long>>
	
	fun getSelectedMonth(): Flow<List<Int>>
	
	fun getTransactions(): Flow<FinancialGroupData>
	
	fun getHighestExpenseCategory(): Flow<Category>
	
	fun getHighestExpenseCategoryAmount(): Flow<Double>
	
	fun getIncomeEntry(): Flow<List<Entry>>
	
	fun getExpenseEntry(): Flow<List<Entry>>
	
	
	suspend fun insertWallet(wallet: Wallet)
	
	suspend fun setSortType(sortType: SortType)
	
	suspend fun setGroupType(groupType: GroupType)
	
	suspend fun setFilterDate(date: Pair<Long, Long>)
	
	suspend fun setSelectedMonth(selectedMonth: List<Int>)
	
}