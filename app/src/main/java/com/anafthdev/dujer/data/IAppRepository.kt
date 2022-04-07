package com.anafthdev.dujer.data

import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.expense.ExpenseRepository
import com.anafthdev.dujer.data.expense.IExpenseRepository
import com.anafthdev.dujer.data.income.IIncomeRepository
import com.anafthdev.dujer.data.income.IncomeRepository
import javax.inject.Inject

interface IAppRepository {
	val appDatastore: AppDatastore
	val incomeRepository: IIncomeRepository
	val expenseRepository: IExpenseRepository
	
	fun delete(vararg financials: Financial)
	
	fun update(vararg financials: Financial)
	
	fun get(id: Int): Financial
	
}

class AppRepository @Inject constructor(
	private val appDatabase: AppDatabase,
	override val appDatastore: AppDatastore
): IAppRepository {
	
	override val incomeRepository: IIncomeRepository by lazy {
		IncomeRepository(appDatabase)
	}
	
	override val expenseRepository: IExpenseRepository by lazy {
		ExpenseRepository(appDatabase)
	}
	
	override fun delete(vararg financials: Financial) {
		financials.forEach { financial ->
			if (financial.type == FinancialType.INCOME) incomeRepository.deleteIncome(financial)
			else expenseRepository.deleteExpense(financial)
		}
	}
	
	override fun update(vararg financials: Financial) {
		financials.forEach { financial ->
			if (financial.type == FinancialType.INCOME) incomeRepository.updateIncome(financial)
			else expenseRepository.updateExpense(financial)
		}
	}
	
	override fun get(id: Int): Financial {
		return appDatabase.financialDao().get(id)
	}
}
