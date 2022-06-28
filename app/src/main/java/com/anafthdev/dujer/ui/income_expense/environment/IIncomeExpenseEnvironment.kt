package com.anafthdev.dujer.ui.income_expense.environment

import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IIncomeExpenseEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getSortType(): Flow<SortType>
	
	fun getGroupType(): Flow<GroupType>
	
	fun getFilterDate(): Flow<Pair<Long, Long>>
	
	fun getSelectedMonth(): Flow<List<Int>>
	
	fun getTransactions(): Flow<FinancialGroupData>
	
	suspend fun setSortType(sortType: SortType)
	
	suspend fun setGroupType(groupType: GroupType)
	
	suspend fun setFilterDate(date: Pair<Long, Long>)
	
	suspend fun setSelectedMonth(selectedMonth: List<Int>)
	
	suspend fun deleteFinancial(vararg financial: Financial)
	
	suspend fun getFinancial(): Flow<Financial>
	
	suspend fun setFinancialID(id: Int)
	
}