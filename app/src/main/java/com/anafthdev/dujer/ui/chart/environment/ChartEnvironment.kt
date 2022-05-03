package com.anafthdev.dujer.ui.chart.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.util.AppUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class ChartEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IChartEnvironment {
	
	private val yearFormatter = SimpleDateFormat("yyyy", AppUtil.deviceLocale)
	
	private var lastYearInMillis = System.currentTimeMillis()
	
	private val incomeList = arrayListOf<Financial>()
	private val expenseList = arrayListOf<Financial>()
	
	private val _filteredIncomeList = MutableLiveData(emptyList<Financial>())
	private val filteredIncomeList: LiveData<List<Financial>> = _filteredIncomeList
	
	private val _filteredExpenseList = MutableLiveData(emptyList<Financial>())
	private val filteredExpenseList: LiveData<List<Financial>> = _filteredExpenseList
	
	init {
		CoroutineScope(dispatcher).launch {
			getIncomeFinancialList().collect { list ->
				incomeList.clear()
				incomeList.addAll(list)
				
				getFilteredIncomeList(lastYearInMillis)
			}
		}
		
		CoroutineScope(dispatcher).launch {
			getExpenseFinancialList().collect { list ->
				expenseList.clear()
				expenseList.addAll(list)
				
				getFilteredExpenseList(lastYearInMillis)
			}
		}
	}
	
	override suspend fun getIncomeFinancialList(): Flow<List<Financial>> {
		return appRepository.expenseRepository.getExpense()
	}
	
	override suspend fun getExpenseFinancialList(): Flow<List<Financial>> {
		return appRepository.incomeRepository.getIncome()
	}
	
	override suspend fun getFilteredIncomeList(yearInMillis: Long) {
		lastYearInMillis = yearInMillis
		
		_filteredIncomeList.postValue(
			incomeList.filter {
				yearFormatter.format(
					it.dateCreated
				) == yearFormatter.format(
					yearInMillis
				)
			}
		)
	}
	
	override suspend fun getFilteredIncomeList(): Flow<List<Financial>> {
		return filteredIncomeList.asFlow()
	}
	
	override suspend fun getFilteredExpenseList(yearInMillis: Long) {
		lastYearInMillis = yearInMillis
		
		_filteredExpenseList.postValue(
			expenseList.filter {
				yearFormatter.format(
					it.dateCreated
				) == yearFormatter.format(
					yearInMillis
				)
			}
		)
	}
	
	override suspend fun getFilteredExpenseList(): Flow<List<Financial>> {
		return filteredExpenseList.asFlow()
	}
	
}