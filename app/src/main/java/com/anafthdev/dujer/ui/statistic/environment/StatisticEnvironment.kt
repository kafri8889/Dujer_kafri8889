package com.anafthdev.dujer.ui.statistic.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.getBy
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class StatisticEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IStatisticEnvironment {
	
	private val _selectedWallet = MutableLiveData(Wallet.cash)
	private val selectedWallet: LiveData<Wallet> = _selectedWallet
	
	private val _incomeTransaction = MutableLiveData(emptyList<Financial>())
	private val incomeTransaction: LiveData<List<Financial>> = _incomeTransaction
	
	private val _expenseTransaction = MutableLiveData(emptyList<Financial>())
	private val expenseTransaction: LiveData<List<Financial>> = _expenseTransaction
	
	private val _availableCategory = MutableLiveData(emptyList<Category>())
	private val availableCategory: LiveData<List<Category>> = _availableCategory
	
	private val _pieEntry = MutableLiveData(emptyList<PieEntry>())
	private val pieEntry: LiveData<List<PieEntry>> = _pieEntry
	
	private val _selectedFinancialType = MutableStateFlow(FinancialType.INCOME)
	private val selectedFinancialType: StateFlow<FinancialType> = _selectedFinancialType
	
	private val _lastSelectedWalletID = MutableStateFlow(Wallet.default.id)
	private val lastSelectedWalletID: StateFlow<Int> = _lastSelectedWalletID
	
	init {
		CoroutineScope(Dispatchers.Main).launch {
			combine(
				appRepository.getAllFinancial(),
				lastSelectedWalletID,
				selectedFinancialType
			) { financials: List<Financial>, walletID: Int, financialType: FinancialType ->
				Triple(financials, walletID, financialType)
			}.collect { triple ->
				Timber.i("collect triple: $triple")
				
				val incomeList = triple.first.filter {
					(it.walletID == triple.second) and (it.type == FinancialType.INCOME)
				}
				
				val expenseList = triple.first.filter {
					(it.walletID == triple.second) and (it.type == FinancialType.EXPENSE)
				}
				
				val categories = (if (triple.third == FinancialType.INCOME) incomeList.getBy {
					it.category
				} else expenseList.getBy { it.category }).distinctBy { it.id }
				
				_availableCategory.postValue(categories)
				_incomeTransaction.postValue(incomeList)
				_expenseTransaction.postValue(expenseList)
				_pieEntry.postValue(
					calculatePieEntry(
						if (triple.third == FinancialType.INCOME) incomeList else expenseList,
						categories
					)
				)
			}
		}
	}
	
	override fun getWallet(walletID: Int) {
		_lastSelectedWalletID.tryEmit(walletID)
		_selectedWallet.postValue(
			appRepository.walletRepository.get(walletID) ?: Wallet.cash
		)
	}
	
	override fun getWallet(): Flow<Wallet> {
		return selectedWallet.asFlow()
	}
	
	override fun getPieEntry(): Flow<List<PieEntry>> {
		return pieEntry.asFlow()
	}
	
	override fun getIncomeTransaction(): Flow<List<Financial>> {
		return incomeTransaction.asFlow()
	}
	
	override fun getExpenseTransaction(): Flow<List<Financial>> {
		return expenseTransaction.asFlow()
	}
	
	override fun getAvailableCategory(): Flow<List<Category>> {
		return availableCategory.asFlow()
	}
	
	override fun getSelectedFinancialType(): Flow<FinancialType> {
		return selectedFinancialType
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