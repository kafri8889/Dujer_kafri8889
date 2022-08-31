package com.anafthdev.dujer.feature.budget_list.environment

import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IBudgetListEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun isTopSnackbarShowed(): Flow<Boolean>
	
	fun getAveragePerMonthCategory(): Flow<List<Pair<Double, Category>>>
	
	
	suspend fun showTopSnackbar(show: Boolean)
	
	suspend fun insertBudget(budget: Budget)
	
}