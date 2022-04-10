package com.anafthdev.dujer.data.repository.income

import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

interface IIncomeRepository {
	
	suspend fun getIncome(): Flow<List<Financial>>
	
	suspend fun newIncome(vararg financial: Financial)
	
	suspend fun deleteIncome(vararg financial: Financial)
	
	suspend fun updateIncome(vararg financial: Financial)
}