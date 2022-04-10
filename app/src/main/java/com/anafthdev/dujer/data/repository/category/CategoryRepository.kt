package com.anafthdev.dujer.data.repository.category

import com.anafthdev.dujer.data.db.AppDatabase
import com.anafthdev.dujer.data.db.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
	private val appDatabase: AppDatabase
): ICategoryRepository {
	
	override suspend fun getAllCategory(): Flow<List<Category>> {
		return appDatabase.categoryDao().getAllCategory()
	}
	
	override suspend fun get(mID: Int): Category {
		return appDatabase.categoryDao().get(mID)
	}
	
	override suspend fun update(vararg category: Category) {
		return appDatabase.categoryDao().update(*category)
	}
	
	override suspend fun delete(vararg category: Category) {
		return appDatabase.categoryDao().delete(*category)
	}
	
	override suspend fun insert(vararg category: Category) {
		return appDatabase.categoryDao().insert(*category)
	}
	
	
}