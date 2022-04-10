package com.anafthdev.dujer.ui.screen.income_expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.common.vibrator.VibratorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
	val vibratorManager: VibratorManager,
	private val appRepository: IAppRepository
): ViewModel() {
	
	private val _incomeFinancialList = MutableLiveData(emptyList<Financial>())
	val incomeFinancialList: LiveData<List<Financial>> = _incomeFinancialList
	
	private val _expenseFinancialList = MutableLiveData(emptyList<Financial>())
	val expenseFinancialList: LiveData<List<Financial>> = _expenseFinancialList
	
	val datastore = appRepository.appDatastore
	
	init {
		viewModelScope.launch {
			appRepository.expenseRepository.getExpense().combine(
				appRepository.incomeRepository.getIncome()
			) { expense, income ->
				expense to income
			}.collect { pair ->
				withContext(Dispatchers.Main) {
					_expenseFinancialList.value = pair.first
					_incomeFinancialList.value = pair.second
				}
			}
		}
	}
	
	fun delete(financial: Financial) {
		viewModelScope.launch {
			appRepository.delete(financial)
		}
	}
	
}