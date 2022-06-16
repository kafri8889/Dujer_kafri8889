package com.anafthdev.dujer.ui.budget.environment

import com.anafthdev.dujer.data.db.model.Budget
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IBudgetEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getBudget(): Flow<Budget>
	
	
	suspend fun setBudget(id: Int)
	
}