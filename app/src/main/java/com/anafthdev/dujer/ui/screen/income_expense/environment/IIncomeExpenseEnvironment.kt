package com.anafthdev.dujer.ui.screen.income_expense.environment

import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IIncomeExpenseEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun deleteFinancial(financial: Financial)
	
	suspend fun getIncomeFinancialList(): Flow<List<Financial>>
	
	suspend fun getExpenseFinancialList(): Flow<List<Financial>>
	
}