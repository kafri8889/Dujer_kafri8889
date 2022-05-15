package com.anafthdev.dujer.ui.financial.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IFinancialEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun updateFinancial(financial: Financial)
	
	suspend fun insertFinancial(financial: Financial)
	
	suspend fun getCategories(): Flow<List<Category>>
	
	fun getWallets(): Flow<List<Wallet>>
	
}