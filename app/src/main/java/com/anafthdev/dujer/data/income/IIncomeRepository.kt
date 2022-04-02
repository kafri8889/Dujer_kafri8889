package com.anafthdev.dujer.data.income

import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

interface IIncomeRepository {
	
	fun getIncome(): Flow<List<Financial>>
	
	fun newIncome(financial: Financial)
}