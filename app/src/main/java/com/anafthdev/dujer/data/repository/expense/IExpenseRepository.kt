package com.anafthdev.dujer.data.repository.expense

import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

interface IExpenseRepository {
	
	suspend fun getExpense(): Flow<List<Financial>>
	
	suspend fun newExpense(vararg financial: Financial)
	
	suspend fun deleteExpense(vararg financial: Financial)
	
	suspend fun updateExpense(vararg financial: Financial)
}