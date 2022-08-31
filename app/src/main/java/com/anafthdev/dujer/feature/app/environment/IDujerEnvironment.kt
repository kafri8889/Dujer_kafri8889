package com.anafthdev.dujer.feature.app.environment

import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.data.UndoType
import com.anafthdev.dujer.model.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IDujerEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getAllBudget(): Flow<List<Budget>>
	
	fun getAllWallet(): Flow<List<Wallet>>
	
	fun getAllCategory(): Flow<List<Category>>
	
	fun getAllFinancial(): Flow<List<Financial>>
	
	fun getCurrentCurrency(): Flow<Currency>
	
	fun getDataCanBeReturned(): Flow<UndoType>
	
	suspend fun updateBudget(vararg budget: Budget)
	
	suspend fun insertWallet(vararg wallet: Wallet)
	
	suspend fun updateWallet(vararg wallet: Wallet)
	
	suspend fun updateFinancial(vararg financial: Financial)
	
	suspend fun deleteFinancial(vararg financial: Financial)
	
	suspend fun deleteCategory(categories: Array<out Category>, financial: Array<out Financial>)
	
	suspend fun deleteWallet(vararg wallet: Wallet)
	
	suspend fun undoFinancial()
	
	suspend fun undoCategory()
	
	suspend fun undoWallet()
	
}