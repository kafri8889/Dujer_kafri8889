package com.anafthdev.dujer.ui.screen.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.AppRepository
import com.anafthdev.dujer.data.datastore.AppDatastore
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.income.IIncomeRepository
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(
	private val appRepository: AppRepository
): ViewModel() {
	
	private val _incomeFinancialList = MutableLiveData(emptyList<Financial>())
	val incomeFinancialList: LiveData<List<Financial>> = _incomeFinancialList
	
	val datastore = appRepository.appDatastore
	
	init {
		viewModelScope.launch {
			appRepository.incomeRepository.getIncome().collect { financialList ->
				_incomeFinancialList.value = financialList
			}
		}
	}
	
}