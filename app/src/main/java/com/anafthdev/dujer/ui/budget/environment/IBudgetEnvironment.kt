package com.anafthdev.dujer.ui.budget.environment

import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Financial
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IBudgetEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getBudget(): Flow<Budget>
	
	fun getSortType(): Flow<SortType>
	
	fun getThisMonthExpenses(): Flow<Double>
	
	fun getAveragePerMonthExpense(): Flow<Double>
	
	fun getSelectedMonth(): Flow<List<Int>>
	
	fun getBarEntries(): Flow<List<BarEntry>>
	
	fun getTransactions(): Flow<List<Financial>>
	
	suspend fun setBudget(id: Int)
	
	suspend fun updateBudget(budget: Budget)
	
	suspend fun deleteBudget(budget: Budget)
	
	suspend fun setSortType(sortType: SortType)
	
	suspend fun setSelectedMonth(selectedMonth: List<Int>)
	
}