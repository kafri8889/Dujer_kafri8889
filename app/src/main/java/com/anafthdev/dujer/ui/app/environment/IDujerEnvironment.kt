package com.anafthdev.dujer.ui.app.environment

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDujerEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	suspend fun getIsFinancialSheetShowed(): Flow<Boolean>
	
	suspend fun getFinancialID(): Flow<Int>
	
	suspend fun getFinancialAction(): Flow<String>
	
	fun setFinancialID(id: Int)
	
	fun setFinancialAction(action: String)
	
	fun showFinancialSheet()
	
	fun hideFinancialSheet()
}