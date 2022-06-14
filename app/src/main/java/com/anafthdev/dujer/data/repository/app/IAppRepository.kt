package com.anafthdev.dujer.data.repository.app

import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.*
import kotlinx.coroutines.flow.Flow

interface IAppRepository {
	val appDatastore: AppDatastore
	val walletRepository: WalletRepository
	val categoryRepository: CategoryRepository
	val incomeRepository: IncomeRepository
	val expenseRepository: ExpenseRepository
	val budgetRepository: BudgetRepository
	
	fun getAllFinancial(): Flow<List<Financial>>
	
	suspend fun get(id: Int): Financial?
	
	suspend fun isExists(financialID: Int): Boolean
	
	suspend fun update(vararg financials: Financial)
	
	suspend fun delete(vararg financials: Financial)
	
	suspend fun insert(vararg financials: Financial)
}

