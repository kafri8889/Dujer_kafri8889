package com.anafthdev.dujer.ui.app.environment

import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.model.Currency
import com.anafthdev.dujer.ui.app.data.UndoType
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
	
	suspend fun insertWallet(vararg wallet: Wallet)
	
	suspend fun updateWallet(vararg wallet: Wallet)
	
	suspend fun updateFinancial(vararg financial: Financial)
	
	suspend fun deleteFinancial(vararg financial: Financial)
	
	suspend fun deleteCategory(vararg category: Category)
	
	suspend fun undoFinancial()
	
	suspend fun undoCategory()
	
	
	
	fun vibrate(millis: Long)
	
}