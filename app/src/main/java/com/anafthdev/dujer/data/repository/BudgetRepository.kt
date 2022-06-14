package com.anafthdev.dujer.data.repository

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Budget
import kotlinx.coroutines.flow.Flow

class BudgetRepository(
	private val appDatabase: AppDatabase
) {
	
	fun getAll(): Flow<List<Budget>> {
		return appDatabase.budgetDAO().getAll()
	}
	
	fun isExists(mID: Int): Boolean {
		return appDatabase.budgetDAO().isExists(mID)
	}
	
	suspend fun get(mID: Int): Budget? {
		return appDatabase.budgetDAO().get(mID)
	}
	
	suspend fun update(vararg budget: Budget) {
		appDatabase.budgetDAO().update(*budget)
	}
	
	suspend fun delete(vararg budget: Budget) {
		appDatabase.budgetDAO().delete(*budget)
	}
	
	suspend fun insert(vararg budget: Budget) {
		appDatabase.budgetDAO().insert(*budget)
	}
	
}