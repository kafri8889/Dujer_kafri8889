package com.anafthdev.dujer.feature.wallet.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.FinancialGroupData
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.data.repository.Repository
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.common.Hexad
import com.anafthdev.dujer.foundation.common.financial_sorter.FinancialSorter
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.combine
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class WalletEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val financialSorter: FinancialSorter,
	private val repository: Repository
): IWalletEnvironment {
	
	private val _selectedWallet = MutableLiveData(Wallet.cash)
	private val selectedWallet: LiveData<Wallet> = _selectedWallet
	
	private val _availableCategory = MutableStateFlow(emptyList<Category>())
	private val availableCategory: StateFlow<List<Category>> = _availableCategory
	
	private val _selectedFinancialType = MutableStateFlow(FinancialType.INCOME)
	private val selectedFinancialType: StateFlow<FinancialType> = _selectedFinancialType
	
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
	
	private val _pieEntry = MutableStateFlow(emptyList<PieEntry>())
	private val pieEntry: StateFlow<List<PieEntry>> = _pieEntry
	
	private val _lastSelectedWalletID = MutableStateFlow(Wallet.default.id)
	private val lastSelectedWalletID: StateFlow<Int> = _lastSelectedWalletID
	
	init {
		CoroutineScope(Dispatchers.Main).launch {
			combine(
				lastSelectedWalletID,
				selectedMonth,
				filterDate,
				selectedSortType,
				selectedGroupType,
				repository.getAllFinancial()
			) { id, month, date, sortType, groupType, financials ->
				Hexad(id, month, date, sortType, groupType, financials)
			}.collect { (id, month, date, sortType, groupType, financials) ->
				val mFinancials = financials.filter { it.walletID == id }
				_transactions.emit(
					financialSorter.beginSort(
						sortType = sortType,
						groupType = groupType,
						filterDate = date,
						selectedMonth = month,
						financials = mFinancials
					)
				)
			}
		}
		
		CoroutineScope(Dispatchers.Main).launch {
			combine(
				repository.getAllFinancial(),
				lastSelectedWalletID,
				selectedFinancialType
			) { financials, walletID, financialType ->
				Triple(financials, walletID, financialType)
			}.collect { (financials, walletID, type) ->
				val incomeList = financials.filter {
					(it.walletID == walletID) and (it.type == FinancialType.INCOME)
				}
				
				val expenseList = financials.filter {
					(it.walletID == walletID) and (it.type == FinancialType.EXPENSE)
				}
				
				val categories = when (type) {
					FinancialType.INCOME -> incomeList.map {
						it.category
					}.distinctBy { it.id }
					FinancialType.EXPENSE ->  expenseList.map {
						it.category
					}.distinctBy { it.id }
					else -> financials.map { it.category }.distinctBy { it.id }
				}
				
				_availableCategory.emit(categories)
				_pieEntry.emit(
					calculatePieEntry(
						when (type) {
							FinancialType.INCOME -> incomeList
							FinancialType.EXPENSE -> expenseList
							else -> financials
						},
						categories
					)
				)
			}
		}
	}
	
	override suspend fun insertFinancial(financial: Financial) {
		repository.insertFinancial(financial)
	}
	
	override suspend fun updateWallet(wallet: Wallet) {
		withContext(Dispatchers.Main) {
			repository.updateWallet(
				wallet.also {
					Timber.i("wallet to update: $wallet")
				}
			)
		}
		
		_selectedWallet.postValue(Wallet.default)
		_selectedWallet.postValue(wallet)
		
	}
	
	override suspend fun deleteWallet(wallet: Wallet) {
		repository.deleteWallet(wallet)
	}
	
	override suspend fun setWalletID(id: Int) {
		_lastSelectedWalletID.emit(Wallet.default.id)
		_lastSelectedWalletID.emit(id)
		
		_selectedWallet.postValue(
			repository.getWalletByID(id) ?: Wallet.cash
		)
	}
	
	override fun getWallet(): Flow<Wallet> {
		return selectedWallet.asFlow()
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
	
	override suspend fun setSelectedFinancialType(type: FinancialType) {
		_selectedFinancialType.emit(type)
	}
	
	override fun getPieEntries(): Flow<List<PieEntry>> {
		return pieEntry
	}
	
	override fun getAvailableCategory(): Flow<List<Category>> {
		return availableCategory
	}
	
	override fun getSelectedFinancialType(): Flow<FinancialType> {
		return selectedFinancialType
	}
	
	private fun calculatePieEntry(
		financials: List<Financial>,
		categories: List<Category>
	): List<PieEntry> {
		val entries = arrayListOf<PieEntry>()
		
		val groupedFinancialAndCategory = arrayListOf<Pair<Category, List<Financial>>>()
		Timber.i("financial and category: $financials, $categories")
		
		categories.forEach { category ->
			groupedFinancialAndCategory.add(
				category to financials.filter { it.category.id == category.id }
			)
		}
		
		groupedFinancialAndCategory.forEach { (category, financials) ->
			val amount = financials.sumOf { it.amount }
			entries.add(
				PieEntry(
					amount.toFloat(),
					category.name,
					category.id to amount
				)
			)
		}
		
		return entries
	}
}