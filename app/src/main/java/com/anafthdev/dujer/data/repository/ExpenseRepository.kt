package com.anafthdev.dujer.data.repository

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class ExpenseRepository(
	private val appDatabase: AppDatabase
) {
	
	fun getExpense(): Flow<List<Financial>> {
		return appDatabase.financialDAO().getExpense().distinctUntilChanged()
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