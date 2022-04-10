package com.anafthdev.dujer.data.repository.expense

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
	private val appDatabase: AppDatabase
): IExpenseRepository {
	
	override suspend fun getExpense(): Flow<List<Financial>> {
		return appDatabase.financialDao().getExpense()
	}
	
	override suspend fun newExpense(vararg financial: Financial) {
		appDatabase.financialDao().insert(*financial)
	}
	
	override suspend fun deleteExpense(vararg financial: Financial) {
		appDatabase.financialDao().delete(*financial)
	}
	
	override suspend fun updateExpense(vararg financial: Financial) {
		appDatabase.financialDao().update(*financial)
	}
	
	
}