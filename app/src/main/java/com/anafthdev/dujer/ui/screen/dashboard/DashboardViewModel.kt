package com.anafthdev.dujer.ui.screen.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.common.vibrator.VibratorManager
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.get
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.util.AppUtil
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
	val vibratorManager: VibratorManager,
	private val appRepository: IAppRepository
): ViewModel() {
	
	private val _incomeFinancialList = MutableLiveData(emptyList<Financial>())
	val incomeFinancialList: LiveData<List<Financial>> = _incomeFinancialList
	
	private val _expenseFinancialList = MutableLiveData(emptyList<Financial>())
	val expenseFinancialList: LiveData<List<Financial>> = _expenseFinancialList
	
	private val _incomeLineDataSetEntry = MutableLiveData(emptyList<Entry>())
	val incomeLineDataSetEntry: LiveData<List<Entry>> = _incomeLineDataSetEntry
	
	private val _expenseLineDataSetEntry = MutableLiveData(emptyList<Entry>())
	val expenseLineDataSetEntry: LiveData<List<Entry>> = _expenseLineDataSetEntry
	
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
					
					calculateIncomeExpenseLineDatasetEntry()
				}
			}
		}
	}
	
	private fun calculateIncomeExpenseLineDatasetEntry() {
		val monthFormatter = SimpleDateFormat("MMM", AppUtil.deviceLocale)
		
		val incomeList = _incomeFinancialList.value ?: emptyList()
		val expenseList = _expenseFinancialList.value ?: emptyList()
		
		val incomeListEntry = arrayListOf<Entry>().apply {
			add(Entry(0f, 0f))
			add(Entry(1f, 0f))
			add(Entry(2f, 0f))
			add(Entry(3f, 0f))
			add(Entry(4f, 0f))
			add(Entry(5f, 0f))
			add(Entry(6f, 0f))
			add(Entry(7f, 0f))
			add(Entry(8f, 0f))
			add(Entry(9f, 0f))
			add(Entry(10f, 0f))
			add(Entry(11f, 0f))
		}
		
		val expenseListEntry = arrayListOf<Entry>().apply {
			add(Entry(0f, 0f))
			add(Entry(1f, 0f))
			add(Entry(2f, 0f))
			add(Entry(3f, 0f))
			add(Entry(4f, 0f))
			add(Entry(5f, 0f))
			add(Entry(6f, 0f))
			add(Entry(7f, 0f))
			add(Entry(8f, 0f))
			add(Entry(9f, 0f))
			add(Entry(10f, 0f))
			add(Entry(11f, 0f))
		}
		
		val monthGroupIncomeList = incomeList.groupBy { monthFormatter.format(it.dateCreated) }
		val monthGroupExpenseList = expenseList.groupBy { monthFormatter.format(it.dateCreated) }
		
		monthGroupIncomeList.forEachMap { k, v ->
			val entryIndex = AppUtil.shortMonths.indexOf { it.contentEquals(k, true) }
			
			incomeListEntry[entryIndex] = Entry(
				entryIndex.toFloat(),
				v.sumOf { it.amount }.toFloat()
			)
		}
		
		monthGroupExpenseList.forEachMap { k, v ->
			val entryIndex = AppUtil.shortMonths.indexOf { it.contentEquals(k, true) }
			
			expenseListEntry[entryIndex] = Entry(
				entryIndex.toFloat(),
				v.sumOf { it.amount }.toFloat()
			)
		}
		
		_incomeLineDataSetEntry.value = incomeListEntry
		_expenseLineDataSetEntry.value = expenseListEntry
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