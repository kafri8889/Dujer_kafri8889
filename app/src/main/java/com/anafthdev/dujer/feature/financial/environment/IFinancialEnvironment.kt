package com.anafthdev.dujer.feature.financial.environment

import com.anafthdev.dujer.data.model.Financial
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IFinancialEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getFinancial(id: Int): Flow<Financial>
	
	suspend fun setFinancial(id: Int)
	
	suspend fun updateFinancial(financial: Financial)
	
	suspend fun insertFinancial(financial: Financial)
	
}