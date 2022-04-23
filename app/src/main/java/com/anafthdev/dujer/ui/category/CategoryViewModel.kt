package com.anafthdev.dujer.ui.category

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.applyElement
import com.anafthdev.dujer.foundation.extension.combine
import com.anafthdev.dujer.foundation.extension.get
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.category.environment.ICategoryEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
	categoryEnvironment: ICategoryEnvironment
): StatefulViewModel<CategoryState, Unit, ICategoryEnvironment>(CategoryState(), categoryEnvironment) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getAll().collect { categories ->
				val categoryIDs = categories.combine(Category.values).getBy { it.id }
				val financialList = environment.getAllFinancial().first()
				
				listenDeletedCategory(financialList, categoryIDs)
				listenModifiedCategory(financialList, categories)
				
				setState {
					copy(
						categories = categories
							.combine(Category.values)
							.distinctBy { it.id }
							.sortedBy { it.name }
					)
				}
			}
		}
	}
	
	private suspend fun listenModifiedCategory(
		financialList: List<Financial>,
		categories: List<Category>
	) {
		environment.updateFinancial(
			*financialList.applyElement { financial ->
				financial.copy(
					category = categories.get { it.id == financial.category.id } ?: Category.other
				)
			}.toTypedArray()
		)
	}
	
	private suspend fun listenDeletedCategory(
		financialList: List<Financial>,
		categoryIDs: List<Int>
	) {
		val filteredFinancial = financialList.filterNot { financial ->
			financial.category.id in categoryIDs
		}
		
		Timber.i("filtered list: $filteredFinancial")
		
		environment.updateFinancial(
			*filteredFinancial.applyElement { financial ->
				financial.copy(
					category = Category.other
				)
			}.toTypedArray()
		)
	}
	
	fun get(id: Int, action: (Category) -> Unit) {
		viewModelScope.launch(environment.dispatcher) {
			environment.get(id, action)
		}
	}
	
	fun updateCategory(category: Category) {
		viewModelScope.launch(environment.dispatcher) {
			environment.update(category)
		}
	}
	
	fun deleteCategory(category: Category) {
		viewModelScope.launch(environment.dispatcher) {
			environment.delete(category)
		}
	}
	
	fun insertCategory(category: Category) {
		viewModelScope.launch(environment.dispatcher) {
			environment.insert(category)
		}
	}
}