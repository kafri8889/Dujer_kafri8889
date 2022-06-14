package com.anafthdev.dujer.data.repository

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
	private val appDatabase: AppDatabase
) {
	
	fun getAllCategory(): Flow<List<Category>> {
		return appDatabase.categoryDAO().getAllCategory()
	}
	
	suspend fun get(mID: Int): Category? {
		return appDatabase.categoryDAO().get(mID)
	}
	
	suspend fun update(vararg category: Category) {
		return appDatabase.categoryDAO().update(*category)
	}
	
	suspend fun delete(vararg category: Category) {
		return appDatabase.categoryDAO().delete(*category)
	}
	
	suspend fun insert(vararg category: Category) {
		return appDatabase.categoryDAO().insert(*category)
	}
	
	
}