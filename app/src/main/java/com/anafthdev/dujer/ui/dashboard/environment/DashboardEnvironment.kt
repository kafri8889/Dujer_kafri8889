package com.anafthdev.dujer.ui.dashboard.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.common.Quint
import com.anafthdev.dujer.foundation.common.financial_sorter.FinancialSorter
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import com.anafthdev.dujer.util.AppUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class DashboardEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val financialSorter: FinancialSorter,
	private val appRepository: IAppRepository
): IDashboardEnvironment {
	
	private val _financial = MutableLiveData(Financial.default)
	private val financial: LiveData<Financial> = _financial
	
	private val _financialAction = MutableLiveData(FinancialAction.NEW)
	private val financialAction: LiveData<String> = _financialAction
	
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
	
	private val _highestExpenseCategory = MutableStateFlow(Category.default)
	private val highestExpenseCategory: StateFlow<Category> = _highestExpenseCategory
	
	private val _highestExpenseCategoryAmount = MutableStateFlow(0.0)
	private val highestExpenseCategoryAmount: StateFlow<Double> = _highestExpenseCategoryAmount
	
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
		
		CoroutineScope(dispatcher).launch {
			appRepository.getAllFinancial().collect { financialList ->
				var highest = Category.default.id to 0.0
				val financials = financialList.filter { it.type == FinancialType.EXPENSE }
				val categories = financials.getBy { it.category }
				val groupedFinancial = financials.groupBy { it.category.id }
				
				groupedFinancial.forEachMap { categoryID, list ->
					val amount = list.sumOf { it.amount }
					
					if (amount > highest.second) {
						highest = categoryID to amount
					}
				}
				
				_highestExpenseCategory.emit(
					categories.find { it.id == highest.first } ?: Category.default
				)
				
				_highestExpenseCategoryAmount.emit(
					highest.second
				)
			}
		}
	}
	
	override fun getFinancial(): Flow<Financial> {
		return financial.asFlow()
	}
	
	override fun getFinancialAction(): Flow<String> {
		return financialAction.asFlow()
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
	
	override fun getHighestExpenseCategory(): Flow<Category> {
		return highestExpenseCategory
	}
	
	override fun getHighestExpenseCategoryAmount(): Flow<Double> {
		return highestExpenseCategoryAmount
	}
	
	override suspend fun setFinancialID(id: Int) {
		_financial.postValue(appRepository.get(id) ?: Financial.default)
	}
	
	override suspend fun insertWallet(wallet: Wallet) {
		appRepository.walletRepository.insertWallet(wallet)
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
	
	override fun setFinancialAction(action: String) {
		_financialAction.postValue(action)
	}
	
}