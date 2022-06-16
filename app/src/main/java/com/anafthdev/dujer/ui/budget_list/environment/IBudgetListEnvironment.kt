package com.anafthdev.dujer.ui.budget_list.environment

import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

interface IBudgetListEnvironment {
	
	val dispatcher: CoroutineDispatcher
	
	fun isTopSnackbarShowed(): Flow<Boolean>
	
	fun getAveragePerMonthCategory(): Flow<List<Pair<Double, Category>>>
	
	
	suspend fun showTopSnackbar(show: Boolean)
	
	suspend fun insertBudget(budget: Budget)
	
}