package com.anafthdev.dujer.ui.dashboard.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.indexOf
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.util.AppUtil
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class DashboardEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_MAIN) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IDashboardEnvironment {
	
	private val _financial = MutableLiveData(Financial.default)
	private val financial: LiveData<Financial> = _financial
	
	private val _financialAction = MutableLiveData(FinancialAction.NEW)
	private val financialAction: LiveData<String> = _financialAction
	
	override suspend fun deleteFinancial(financial: Financial) {
		appRepository.delete(financial)
	}
	
	override suspend fun getFinancial(): Flow<Financial> {
		return financial.asFlow()
	}
	
	override suspend fun getFinancialAction(): Flow<String> {
		return financialAction.asFlow()
	}
	
	override suspend fun getUserBalance(): Flow<Double> {
		return appRepository.appDatastore.getUserBalance
	}
	
	override suspend fun getIncomeFinancialList(): Flow<List<Financial>> {
		return appRepository.incomeRepository.getIncome()
	}
	
	override suspend fun getExpenseFinancialList(): Flow<List<Financial>> {
		return appRepository.expenseRepository.getExpense()
	}
	
	override suspend fun getLineDataSetEntry(
		incomeList: List<Financial>,
		expenseList: List<Financial>
	): Pair<List<Entry>, List<Entry>> {
		val monthFormatter = SimpleDateFormat("MMM", AppUtil.deviceLocale)
		
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
		
		return incomeListEntry to expenseListEntry
	}
	
	override suspend fun setFinancialID(id: Int) {
		_financial.value = appRepository.get(id) ?: Financial.default
	}
	
	override fun setFinancialAction(action: String) {
		_financialAction.value = action
	}
	
}