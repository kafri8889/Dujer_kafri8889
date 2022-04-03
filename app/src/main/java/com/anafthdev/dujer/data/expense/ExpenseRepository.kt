package com.anafthdev.dujer.data.expense

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
	private val appDatabase: AppDatabase
): IExpenseRepository {
	
	override fun getExpense(): Flow<List<Financial>> {
		return appDatabase.financialDao().getExpense()
	}
	
	override fun newExpense(vararg financial: Financial) {
		appDatabase.financialDao().insert(*financial)
	}
	
	override fun deleteExpense(vararg financial: Financial) {
		appDatabase.financialDao().delete(*financial)
	}
	
	override fun updateExpense(vararg financial: Financial) {
		appDatabase.financialDao().update(*financial)
	}
	
	
}