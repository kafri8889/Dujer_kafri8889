package com.anafthdev.dujer.ui.category.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class CategoryEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_MAIN) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): ICategoryEnvironment {
	
	override suspend fun get(id: Int, action: (Category) -> Unit) {
		if (id != Category.default.id) {
			val category = appRepository.categoryRepository.get(id)
			action(category)
		}
	}
	
	override suspend fun updateFinancial(vararg financial: Financial) {
		appRepository.update(*financial)
	}
	
	override suspend fun getAllFinancial(): Flow<List<Financial>> {
		return appRepository.getAllFinancial()
	}
	
	override suspend fun getAll(): Flow<List<Category>> {
		return appRepository.categoryRepository.getAllCategory()
	}
	
	override suspend fun update(category: Category) {
		appRepository.categoryRepository.update(category)
	}
	
	override suspend fun delete(category: Category) {
		appRepository.categoryRepository.delete(category)
	}
	
	override suspend fun insert(category: Category) {
		appRepository.categoryRepository.insert(category)
	}
	
}