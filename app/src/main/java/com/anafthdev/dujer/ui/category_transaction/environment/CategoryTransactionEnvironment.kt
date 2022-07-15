package com.anafthdev.dujer.ui.category_transaction.environment

import com.anafthdev.dujer.data.db.model.Category
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
	
	override fun getCategory(): Flow<Category> {
		return category
	}
	
	override suspend fun setCategory(id: Int) {
		_category.emit(
			getDefaultCategory(id) {
				appRepository.categoryRepository.get(id) ?: Category.default
			}
		)
	}
	
	private inline fun getDefaultCategory(id: Int, ifNotDefault: () -> Category): Category {
		Category.values.forEach {
			if (id == it.id) return it
		}
		
		return ifNotDefault()
	}
	
}