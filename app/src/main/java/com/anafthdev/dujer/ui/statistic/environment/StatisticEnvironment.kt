package com.anafthdev.dujer.ui.statistic.environment

import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.common.Quad
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.extension.getBy
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class StatisticEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_MAIN) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IStatisticEnvironment {
	
	private val monthYearFormatter = SimpleDateFormat("MMM yyyy", deviceLocale)
	
	private val _selectedWallet = MutableStateFlow(Wallet.cash)
	private val selectedWallet: StateFlow<Wallet> = _selectedWallet
	
	private val _incomeTransaction = MutableStateFlow(emptyList<Financial>())
	private val incomeTransaction: StateFlow<List<Financial>> = _incomeTransaction
	
	private val _expenseTransaction = MutableStateFlow(emptyList<Financial>())
	private val expenseTransaction: StateFlow<List<Financial>> = _expenseTransaction
	
	private val _availableCategory = MutableStateFlow(emptyList<Category>())
	private val availableCategory: StateFlow<List<Category>> = _availableCategory
	
	private val _pieEntry = MutableStateFlow(emptyList<PieEntry>())
	private val pieEntry: StateFlow<List<PieEntry>> = _pieEntry
	
	private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
	private val selectedDate: StateFlow<Long> = _selectedDate
	
	private val _selectedFinancialType = MutableStateFlow(FinancialType.INCOME)
	private val selectedFinancialType: StateFlow<FinancialType> = _selectedFinancialType
	
	private val _lastSelectedWalletID = MutableStateFlow(Wallet.default.id)
	private val lastSelectedWalletID: StateFlow<Int> = _lastSelectedWalletID
	
	init {
		CoroutineScope(dispatcher).launch {
			combine(
				appRepository.getAllFinancial(),
				lastSelectedWalletID,
				selectedFinancialType,
				selectedDate
			) { financials, walletID, financialType, selectedDate ->
				Quad(financials, walletID, financialType, selectedDate)
			}.collect { quad ->
				
				val incomeList = quad.first.filter {
					(it.walletID == quad.second)
						.and(it.type == FinancialType.INCOME)
						.and(
							monthYearFormatter.format(it.dateCreated).equals(
								other = monthYearFormatter.format(quad.fourth),
								ignoreCase = true
							)
						)
				}
				
				val expenseList = quad.first.filter {
					(it.walletID == quad.second)
						.and(it.type == FinancialType.EXPENSE)
						.and(
							monthYearFormatter.format(it.dateCreated).equals(
								other = monthYearFormatter.format(quad.fourth),
								ignoreCase = true
							)
						)
				}
				
				val categories = (if (quad.third == FinancialType.INCOME) incomeList.getBy {
					it.category
				} else expenseList.getBy { it.category }).distinctBy { it.id }
				
				_availableCategory.emit(categories)
				_incomeTransaction.emit(incomeList)
				_expenseTransaction.emit(expenseList)
				_pieEntry.emit(
					calculatePieEntry(
						if (quad.third == FinancialType.INCOME) incomeList else expenseList,
						categories
					)
				)
			}
		}
	}
	
	override suspend fun getWallet(walletID: Int) {
		_lastSelectedWalletID.tryEmit(walletID)
		withContext(Dispatchers.IO) {
			_selectedWallet.emit(
				appRepository.walletRepository.get(walletID) ?: Wallet.cash
			)
		}
	}
	
	override fun getWallet(): Flow<Wallet> {
		return selectedWallet
	}
	
	override fun getPieEntry(): Flow<List<PieEntry>> {
		return pieEntry
	}
	
	override fun getIncomeTransaction(): Flow<List<Financial>> {
		return incomeTransaction
	}
	
	override fun getExpenseTransaction(): Flow<List<Financial>> {
		return expenseTransaction
	}
	
	override fun getAvailableCategory(): Flow<List<Category>> {
		return availableCategory
	}
	
	override fun getSelectedFinancialType(): Flow<FinancialType> {
		return selectedFinancialType
	}
	
	override suspend fun setSelectedDate(date: Long) {
		_selectedDate.emit(date)
	}
	
	override suspend fun setSelectedFinancialType(type: FinancialType) {
		_selectedFinancialType.emit(type)
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
			entries.add(
				PieEntry(
					financials.sumOf { it.amount }.toFloat(),
					category.name
				)
			)
		}
		
		return entries
	}
	
}