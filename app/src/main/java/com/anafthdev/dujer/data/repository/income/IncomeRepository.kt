package com.anafthdev.dujer.data.repository.income

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

class IncomeRepository(
	private val appDatabase: AppDatabase
): IIncomeRepository {
	
	override suspend fun getIncome(): Flow<List<Financial>> {
		return appDatabase.financialDao().getIncome()
	}
	
	override suspend fun newIncome(vararg financial: Financial) {
		appDatabase.financialDao().insert(*financial)
	}
	
	override suspend fun deleteIncome(vararg financial: Financial) {
		appDatabase.financialDao().delete(*financial)
	}
	
	override suspend fun updateIncome(vararg financial: Financial) {
		appDatabase.financialDao().update(*financial)
	}
}