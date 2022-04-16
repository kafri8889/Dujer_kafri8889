package com.anafthdev.dujer.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.extension.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
	private val appRepository: IAppRepository
) : ViewModel() {
	
	private val _categories = MutableLiveData(emptyList<Category>())
	val categories: LiveData<List<Category>> = _categories
	
	init {
		viewModelScope.launch {
			appRepository.categoryRepository.getAllCategory().collect { categoryList ->
				_categories.value = categoryList.combine(Category.values).distinctBy { it.id }
			}
		}
	}
	
}