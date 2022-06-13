package com.anafthdev.dujer.ui.dashboard.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDashboardEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getFinancial(): Flow<Financial>
	
	suspend fun getFinancialAction(): Flow<String>
	
	suspend fun getHighestExpenseCategory(): Flow<Category>
	
	suspend fun getHighestExpenseCategoryAmount(): Flow<Double>
	
	
	suspend fun setFinancialID(id: Int)
	
	suspend fun insertWallet(wallet: Wallet)
	
	fun setFinancialAction(action: String)
	
}