package com.anafthdev.dujer.ui.budget_list.environment

import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IBudgetListEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun getAveragePerMonthCategory(): Flow<List<Pair<Double, Category>>>
	
}