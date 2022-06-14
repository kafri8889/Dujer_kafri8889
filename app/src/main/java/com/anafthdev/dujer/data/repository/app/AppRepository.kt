package com.anafthdev.dujer.data.repository.app

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class AppRepository @Inject constructor(
	private val appDatabase: AppDatabase,
	override val appDatastore: AppDatastore
): IAppRepository {
	
	override val walletRepository: WalletRepository by lazy {
		WalletRepository(appDatabase)
	}
	
	override val categoryRepository: CategoryRepository by lazy {
		CategoryRepository(appDatabase)
	}
	
	override val incomeRepository: IncomeRepository by lazy {
		IncomeRepository(appDatabase)
	}
	
	override val expenseRepository: ExpenseRepository by lazy {
		ExpenseRepository(appDatabase)
	}
	
	override val budgetRepository: BudgetRepository by lazy {
		BudgetRepository(appDatabase)
	}
	
	override fun getAllFinancial(): Flow<List<Financial>> {
		return appDatabase.financialDAO().getAll().distinctUntilChanged()
	}
	
	override suspend fun get(id: Int): Financial? {
		return appDatabase.financialDAO().get(id)
	}
	
	override suspend fun isExists(financialID: Int): Boolean {
		return appDatabase.financialDAO().isExists(financialID)
	}
	
	override suspend fun update(vararg financials: Financial) {
		financials.forEach { financial ->
			if (financial.type == FinancialType.INCOME) incomeRepository.updateIncome(financial)
			else expenseRepository.updateExpense(financial)
		}
	}
	
	override suspend fun delete(vararg financials: Financial) {
		financials.forEach { financial ->
			if (financial.type == FinancialType.INCOME) incomeRepository.deleteIncome(financial)
			else expenseRepository.deleteExpense(financial)
		}
	}
	
	override suspend fun insert(vararg financials: Financial) {
		financials.forEach { financial ->
			if (financial.type == FinancialType.INCOME) incomeRepository.newIncome(financial)
			else expenseRepository.newExpense(financial)
		}
	}
}