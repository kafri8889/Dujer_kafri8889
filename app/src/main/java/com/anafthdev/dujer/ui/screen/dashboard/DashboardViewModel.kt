package com.anafthdev.dujer.ui.screen.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.foundation.common.vibrator.VibratorManager
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
	val vibratorManager: VibratorManager,
	private val appRepository: IAppRepository
): ViewModel() {

	private val _mixedFinancialList = MutableLiveData(emptyList<Financial>())
	val mixedFinancialList: LiveData<List<Financial>> = _mixedFinancialList
	
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
					_mixedFinancialList.value = pair.first.combine(pair.second)
					Timber.i("financials: ${_mixedFinancialList.value}")
				}
			}
		}
	}
	
	fun newRecord(financial: Financial) {
		viewModelScope.launch {
			if (financial.type == FinancialType.INCOME) appRepository.incomeRepository.newIncome(financial)
			else appRepository.expenseRepository.newExpense(financial)
		}
	}
	
	fun deleteRecord(financial: Financial) {
		viewModelScope.launch {
			appRepository.delete(financial)
		}
	}
}