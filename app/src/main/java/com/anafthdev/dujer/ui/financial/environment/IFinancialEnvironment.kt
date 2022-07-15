package com.anafthdev.dujer.ui.financial.environment

import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IFinancialEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getFinancial(): Flow<Financial>
	
	suspend fun getFinancial(id: Int)
	
	suspend fun updateFinancial(financial: Financial)
	
	suspend fun insertFinancial(financial: Financial)
	
}