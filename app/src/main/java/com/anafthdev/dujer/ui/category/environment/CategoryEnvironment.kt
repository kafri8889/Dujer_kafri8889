package com.anafthdev.dujer.ui.category.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class CategoryEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): ICategoryEnvironment {
	
	override suspend fun getAllCategory(): Flow<List<Category>> {
		return appRepository.categoryRepository.getAllCategory()
	}
	
	override suspend fun updateCategory(category: Category) {
		appRepository.categoryRepository.update(category)
	}
	
	override suspend fun deleteCategory(category: Category) {
		appRepository.categoryRepository.delete(category)
	}
	
	override suspend fun insertCategory(category: Category) {
		appRepository.categoryRepository.insert(category)
	}
	
}