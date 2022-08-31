package com.anafthdev.dujer.feature.category.environment

import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.repository.Repository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class CategoryEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val repository: Repository
): ICategoryEnvironment {
	
	private val _selectedCategory = MutableStateFlow(Category.default)
	private val selectedCategory: StateFlow<Category> = _selectedCategory
	
	override fun getCategory(): Flow<Category> {
		return selectedCategory
	}
	
	override suspend fun setCategory(id: Int) {
		_selectedCategory.emit(repository.getCategoryByID(id) ?: Category.default)
	}
	
	override suspend fun updateFinancial(vararg financial: Financial) {
		repository.updateFinancial(*financial)
	}
	
	override suspend fun update(vararg category: Category) {
		repository.updateCategory(*category)
	}
	
	override suspend fun insert(vararg category: Category) {
		repository.insertCategory(*category)
	}
	
}