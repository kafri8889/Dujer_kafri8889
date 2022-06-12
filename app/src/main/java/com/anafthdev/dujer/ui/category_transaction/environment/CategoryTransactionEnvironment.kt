package com.anafthdev.dujer.ui.category_transaction.environment

import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named

class CategoryTransactionEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): ICategoryTransactionEnvironment {
	
	private val _category = MutableStateFlow(Category.default)
	private val category: StateFlow<Category> = _category
	
	private val _selectedFinancial = MutableStateFlow(Financial.default)
	private val selectedFinancial: StateFlow<Financial> = _selectedFinancial
	
	override fun getCategory(): Flow<Category> {
		return category
	}
	
	override fun getFinancial(): Flow<Financial> {
		return selectedFinancial
	}
	
	override suspend fun setCategory(id: Int) {
		_category.emit(
			getDefaultCategory(id) {
				appRepository.categoryRepository.get(id) ?: Category.default
			}
		)
	}
	
	override suspend fun setFinancial(id: Int) {
		_selectedFinancial.emit(
			appRepository.get(id) ?: Financial.default
		)
	}
	
	private inline fun getDefaultCategory(id: Int, ifNotDefault: () -> Category): Category {
		Category.values.forEach {
			if (id == it.id) return it
		}
		
		return ifNotDefault()
	}
	
}