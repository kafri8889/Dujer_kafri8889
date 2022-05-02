package com.anafthdev.dujer.ui.chart.environment

import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IChartEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getIncomeFinancialList(): Flow<List<Financial>>
	
	suspend fun getExpenseFinancialList(): Flow<List<Financial>>
	
}