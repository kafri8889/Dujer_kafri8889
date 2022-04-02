package com.anafthdev.dujer.data.expense

import com.anafthdev.dujer.data.db.model.Financial
import kotlinx.coroutines.flow.Flow

interface IExpenseRepository {
	
	fun getExpense(): Flow<List<Financial>>
	
	fun newExpense(financial: Financial)
}