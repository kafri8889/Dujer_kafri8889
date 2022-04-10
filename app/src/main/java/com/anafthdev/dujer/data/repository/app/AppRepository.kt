package com.anafthdev.dujer.data.repository.app

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.category.CategoryRepository
import com.anafthdev.dujer.data.repository.category.ICategoryRepository
import com.anafthdev.dujer.data.repository.expense.ExpenseRepository
import com.anafthdev.dujer.data.repository.expense.IExpenseRepository
import com.anafthdev.dujer.data.repository.income.IIncomeRepository
import com.anafthdev.dujer.data.repository.income.IncomeRepository
import javax.inject.Inject

class AppRepository @Inject constructor(
	private val appDatabase: AppDatabase,
	override val appDatastore: AppDatastore
): IAppRepository {
	
	override val categoryRepository: ICategoryRepository by lazy {
		CategoryRepository(appDatabase)
	}
	
	override val incomeRepository: IIncomeRepository by lazy {
		IncomeRepository(appDatabase)
	}
	
	override val expenseRepository: IExpenseRepository by lazy {
		ExpenseRepository(appDatabase)
	}
	
	override suspend fun get(id: Int): Financial {
		return appDatabase.financialDao().get(id)
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