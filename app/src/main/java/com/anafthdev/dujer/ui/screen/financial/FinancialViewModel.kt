package com.anafthdev.dujer.ui.screen.financial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FinancialViewModel @Inject constructor(
	private val appRepository: IAppRepository
): ViewModel() {
	
	private val _financial = MutableLiveData(Financial.default)
	val financial: LiveData<Financial> = _financial
	
	private val _categories = MutableLiveData(emptyList<Category>())
	val categories: LiveData<List<Category>> = _categories
	
	val currentCurrency: Flow<com.anafthdev.dujer.model.Currency> = appRepository.appDatastore.getCurrentCurrency
	val deviceCurrency: Currency = Currency.getInstance(AppUtil.deviceLocale)
	
	init {
		viewModelScope.launch {
			appRepository.categoryRepository.getAllCategory().collect{ categoryList ->
				_categories.value = categoryList
			}
		}
	}
	
	fun getFinancial(id: Int, action: (Financial) -> Unit) {
		viewModelScope.launch {
			_financial.value = appRepository.get(id).also(action)
		}
	}
	
	fun updateFinancial(financial: Financial, action: () -> Unit) {
		viewModelScope.launch {
			appRepository.update(financial)
			action()
		}
	}
	
	fun insertFinancial(financial: Financial, action: () -> Unit) {
		viewModelScope.launch {
			appRepository.insert(financial)
			action()
		}
	}
	
	companion object {
		const val FINANCIAL_ACTION_NEW = "new"
		const val FINANCIAL_ACTION_EDIT = "edit"
	}
	
}