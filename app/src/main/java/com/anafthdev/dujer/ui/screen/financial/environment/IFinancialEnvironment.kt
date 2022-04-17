package com.anafthdev.dujer.ui.screen.financial.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IFinancialEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getFinancial(id: Int, action: (Financial) -> Unit)
	
	suspend fun updateFinancial(financial: Financial, action: () -> Unit)
	
	suspend fun insertFinancial(financial: Financial, action: () -> Unit)
	
	suspend fun getCategories(): Flow<List<Category>>
	
	suspend fun getCurrentCurrency(): Flow<Currency>
}