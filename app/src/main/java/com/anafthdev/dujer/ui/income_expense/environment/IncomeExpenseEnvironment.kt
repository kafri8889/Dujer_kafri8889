package com.anafthdev.dujer.ui.income_expense.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.common.Quint
import com.anafthdev.dujer.foundation.common.financial_sorter.FinancialSorter
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class IncomeExpenseEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_MAIN) override val dispatcher: CoroutineDispatcher,
	private val financialSorter: FinancialSorter,
	private val appRepository: IAppRepository
): IIncomeExpenseEnvironment {
	
	private val _financial = MutableLiveData(Financial.default)
	private val financial: LiveData<Financial> = _financial
	
	private val _selectedSortType = MutableStateFlow(SortType.A_TO_Z)
	private val selectedSortType: StateFlow<SortType> = _selectedSortType
	
	private val _selectedGroupType = MutableStateFlow(GroupType.DEFAULT)
	private val selectedGroupType: StateFlow<GroupType> = _selectedGroupType
	
	private val _selectedMonth = MutableStateFlow(
		listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
	)
	private val selectedMonth: StateFlow<List<Int>> = _selectedMonth
	
	private val _filterDate = MutableStateFlow(AppUtil.filterDateDefault)
	private val filterDate: StateFlow<Pair<Long, Long>> = _filterDate
	
	private val _transactions = MutableStateFlow(FinancialGroupData.default)
	private val transactions: StateFlow<FinancialGroupData> = _transactions
	
	init {
		CoroutineScope(dispatcher).launch {
			combine(
				selectedMonth,
				filterDate,
				selectedSortType,
				selectedGroupType,
				appRepository.getAllFinancial()
			) { month, year, sortType, groupType, financials ->
				Quint(month, year, sortType, groupType, financials)
			}.collect { (month, year, sortType, groupType, financials) ->
				_transactions.emit(
					financialSorter.beginSort(
						sortType = sortType,
						groupType = groupType,
						filterDate = year,
						selectedMonth = month,
						financials = financials
					)
				)
			}
		}
	}
	
	override fun getSortType(): Flow<SortType> {
		return selectedSortType
	}
	
	override fun getGroupType(): Flow<GroupType> {
		return selectedGroupType
	}
	
	override fun getFilterDate(): Flow<Pair<Long, Long>> {
		return filterDate
	}
	
	override fun getSelectedMonth(): Flow<List<Int>> {
		return selectedMonth
	}
	
	override fun getTransactions(): Flow<FinancialGroupData> {
		return transactions
	}
	
	override suspend fun setSortType(sortType: SortType) {
		_selectedSortType.emit(sortType)
	}
	
	override suspend fun setGroupType(groupType: GroupType) {
		_selectedGroupType.emit(groupType)
	}
	
	override suspend fun setFilterDate(date: Pair<Long, Long>) {
		_filterDate.emit(date)
	}
	
	override suspend fun setSelectedMonth(selectedMonth: List<Int>) {
		_selectedMonth.emit(selectedMonth)
	}
	
	override suspend fun deleteFinancial(vararg financial: Financial) {
		appRepository.delete(*financial)
	}
	
	override suspend fun getFinancial(): Flow<Financial> {
		return financial.asFlow()
	}
	
	override suspend fun setFinancialID(id: Int) {
		_financial.value = appRepository.get(id) ?: Financial.default
	}
	
}