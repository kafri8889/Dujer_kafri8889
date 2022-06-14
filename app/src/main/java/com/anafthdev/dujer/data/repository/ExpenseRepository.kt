package com.anafthdev.dujer.data.repository

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
	private val appDatabase: AppDatabase
) {
	
	suspend fun getExpense(): Flow<List<Financial>> {
		return appDatabase.financialDAO().getExpense()
	}
	
	suspend fun newExpense(vararg financial: Financial) {
		appDatabase.financialDAO().insert(*financial)
	}
	
	suspend fun deleteExpense(vararg financial: Financial) {
		appDatabase.financialDAO().delete(*financial)
	}
	
	suspend fun updateExpense(vararg financial: Financial) {
		appDatabase.financialDAO().update(*financial)
	}
	
	
}