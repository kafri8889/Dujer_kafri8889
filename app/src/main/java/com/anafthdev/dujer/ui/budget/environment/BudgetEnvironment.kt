package com.anafthdev.dujer.ui.budget.environment

import com.anafthdev.dujer.data.GroupType
import com.anafthdev.dujer.data.SortType
import com.anafthdev.dujer.data.db.model.Budget
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.common.AppUtil
import com.anafthdev.dujer.foundation.common.Hexad
import com.anafthdev.dujer.foundation.common.financial_sorter.FinancialSorter
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.averageDouble
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.indexOf
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class BudgetEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val financialSorter: FinancialSorter,
	private val appRepository: IAppRepository
): IBudgetEnvironment {
	
	private val monthFormatter = SimpleDateFormat("MMM", deviceLocale)
	
	private val _selectedBudget = MutableStateFlow(Budget.defalut)
	private val selectedBudget: StateFlow<Budget> = _selectedBudget
	
	private val _selectedSortType = MutableStateFlow(SortType.A_TO_Z)
	private val selectedSortType: StateFlow<SortType> = _selectedSortType
	
	private val _selectedGroupType = MutableStateFlow(GroupType.DEFAULT)
	private val selectedGroupType: StateFlow<GroupType> = _selectedGroupType
	
	private val _filterDate = MutableStateFlow(AppUtil.filterDateDefault)
	private val filterDate: StateFlow<Pair<Long, Long>> = _filterDate
	
	private val _thisMonthExpenses = MutableStateFlow(0.0)
	private val thisMonthExpenses: StateFlow<Double> = _thisMonthExpenses
	
	private val _averagePerMonthExpense = MutableStateFlow(0.0)
	private val averagePerMonthExpense: StateFlow<Double> = _averagePerMonthExpense
	
	private val _selectedMonth = MutableStateFlow(
		listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
	)
	private val selectedMonth: StateFlow<List<Int>> = _selectedMonth
	
	private val _barEntries = MutableStateFlow(emptyList<BarEntry>())
	private val barEntries: StateFlow<List<BarEntry>> = _barEntries
	
	private val _transactions = MutableStateFlow(emptyList<Financial>())
	private val transactions: StateFlow<List<Financial>> = _transactions
	
	private val barEntryTemplate = arrayListOf<BarEntry>().apply {
		for (i in 0..11) {
			add(
				BarEntry(
					i.toFloat(),
					0f
				)
			)
		}
	}
	
	init {
		CoroutineScope(dispatcher).launch {
			combine(
				selectedBudget,
				appRepository.getAllFinancial()
			) { budget, financials ->
				budget to financials
			}.collect { (budget, financials) ->
				val filteredFinancials = financials.filter { it.category.id == budget.category.id }
				val entry = ArrayList(barEntryTemplate)
				val averageList = arrayListOf<Double>()
				
				val groupedFinancial = filteredFinancials.groupBy { monthFormatter.format(it.dateCreated) }
				
				groupedFinancial.forEachMap { k, v ->
					val totalAmount = v.sumOf { it.amount }
					val entryIndex = AppUtil.shortMonths.indexOf { it.contentEquals(k, true) }
					Timber.i("entry I: $entryIndex")
					
					entry[entryIndex] = BarEntry(
						entryIndex.toFloat(),
						totalAmount.toFloat()
					)
					
					averageList.add(totalAmount)
				}
				
				Timber.i("isnan val: $averageList, sum: ${averageList.sum()}, res: ${
					averageList.sum() / averageList.size
				}, nan: ${(averageList.sum() / averageList.size).isNaN()}")
				
				_barEntries.emit(entry)
				_thisMonthExpenses.emit(
					financials
						.asSequence()
						.filter { it.category.id == budget.category.id }
						.filter {
							monthFormatter.format(it.dateCreated)
								.equals(monthFormatter.format(System.currentTimeMillis()))
						}.sumOf { it.amount }
				)
				
				_averagePerMonthExpense.emit(
					averageList.averageDouble { averageList.average().toInt().toDouble() }
				)
			}
		}
		
		CoroutineScope(dispatcher).launch {
			com.anafthdev.dujer.foundation.extension.combine(
				selectedMonth,
				filterDate,
				selectedBudget,
				selectedSortType,
				selectedGroupType,
				appRepository.getAllFinancial()
			) { month, date, budget, sortType, groupType, financials ->
				Hexad(month, date, budget, sortType, groupType, financials)
			}.collect { (month, date, budget, sortType, groupType, financials) ->
				val filteredFinancials = financials.filter {
					it.category.id == budget.category.id
				}
				
//				_transactions.emit(
//					financialSorter.beginSort(
//						sortType = sortType,
//						groupType = groupType,
//						filterDate = date,
//						selectedMonth = month,
//						financials = filteredFinancials
//					)
//				)
			}
		}
	}
	
	override fun getBudget(): Flow<Budget> {
		return selectedBudget
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
	
	override fun getThisMonthExpenses(): Flow<Double> {
		return thisMonthExpenses
	}
	
	override fun getAveragePerMonthExpense(): Flow<Double> {
		return averagePerMonthExpense
	}
	
	override fun getSelectedMonth(): Flow<List<Int>> {
		return selectedMonth
	}
	
	override fun getBarEntries(): Flow<List<BarEntry>> {
		return barEntries
	}
	
	override fun getTransactions(): Flow<List<Financial>> {
		return transactions
	}
	
	override suspend fun setBudget(id: Int) {
		_selectedBudget.emit(
			appRepository.budgetRepository.get(id) ?: Budget.defalut
		)
	}
	
	override suspend fun updateBudget(budget: Budget) {
		appRepository.budgetRepository.update(budget)
		_selectedBudget.emit(Budget.defalut)
		_selectedBudget.emit(
			appRepository.budgetRepository.get(budget.id) ?: Budget.defalut
		)
	}
	
	override suspend fun deleteBudget(budget: Budget) {
		appRepository.budgetRepository.delete(budget)
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
	
}