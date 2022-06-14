package com.anafthdev.dujer.data.repository

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

class IncomeRepository(
	private val appDatabase: AppDatabase
) {
	
	suspend fun getIncome(): Flow<List<Financial>> {
		return appDatabase.financialDAO().getIncome()
	}
	
	suspend fun newIncome(vararg financial: Financial) {
		appDatabase.financialDAO().insert(*financial)
	}
	
	suspend fun deleteIncome(vararg financial: Financial) {
		appDatabase.financialDAO().delete(*financial)
	}
	
	suspend fun updateIncome(vararg financial: Financial) {
		appDatabase.financialDAO().update(*financial)
	}
}