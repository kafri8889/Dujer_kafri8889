package com.anafthdev.dujer.ui.screen.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.AppRepository
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.extension.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
	private val appRepository: AppRepository
): ViewModel() {

	private val _mixedFinancialList = MutableLiveData(emptyList<Financial>())
	val mixedFinancialList: LiveData<List<Financial>> = _mixedFinancialList
	
	val datastore = appRepository.appDatastore
	
	init {
		viewModelScope.launch {
			appRepository.expenseRepository.getExpense().combine(
				appRepository.incomeRepository.getIncome()
			) { expense, income ->
				expense.combine(income)
			}.collect { financialList ->
				withContext(Dispatchers.Main) {
					_mixedFinancialList.value = financialList
					Timber.i("financials: $financialList")
				}
			}
		}
	}
	
	fun newRecord(financial: Financial) {
		if (financial.type == FinancialType.INCOME) appRepository.incomeRepository.newIncome(financial)
		else appRepository.expenseRepository.newExpense(financial)
	}
}