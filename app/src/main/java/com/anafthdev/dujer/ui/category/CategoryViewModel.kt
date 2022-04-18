package com.anafthdev.dujer.ui.category

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.foundation.extension.combine
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.ui.category.environment.ICategoryEnvironment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
	categoryEnvironment: ICategoryEnvironment
): StatefulViewModel<CategoryState, ICategoryEnvironment>(CategoryState(), categoryEnvironment) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getAllCategory().collect { categories ->
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
	
	fun updateCategory(category: Category) {
		viewModelScope.launch(environment.dispatcher) {
			environment.updateCategory(category)
		}
	}
	
	fun insertCategory(category: Category) {
		viewModelScope.launch(environment.dispatcher) {
			environment.insertCategory(category)
		}
	}
}