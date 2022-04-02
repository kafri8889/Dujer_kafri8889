package com.anafthdev.dujer.data.income

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

class IncomeRepository(
	private val appDatabase: AppDatabase
): IIncomeRepository {
	
	override fun getIncome(): Flow<List<Financial>> {
		return appDatabase.financialDao().getIncome()
	}
	
	override fun newIncome(financial: Financial) {
		appDatabase.financialDao().insert(financial)
	}
}