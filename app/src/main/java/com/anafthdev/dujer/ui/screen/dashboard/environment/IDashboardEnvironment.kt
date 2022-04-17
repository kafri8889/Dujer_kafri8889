package com.anafthdev.dujer.ui.screen.dashboard.environment

import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.model.Currency
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDashboardEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun deleteRecord(financial: Financial)
	
	suspend fun getUserBalance(): Flow<Double>
	
	suspend fun getCurrentCurrency(): Flow<Currency>
	
	suspend fun getIncomeFinancialList(): Flow<List<Financial>>
	
	suspend fun getExpenseFinancialList(): Flow<List<Financial>>
	
	suspend fun getLineDataSetEntry(
		incomeList: List<Financial>,
		expenseList: List<Financial>
	): Pair<List<Entry>, List<Entry>>
	
}