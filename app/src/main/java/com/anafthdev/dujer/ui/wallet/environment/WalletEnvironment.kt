package com.anafthdev.dujer.ui.wallet.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.common.Quad
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.foundation.extension.merge
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
	private val appRepository: IAppRepository
): IWalletEnvironment {
	
	private val _selectedWallet = MutableLiveData(Wallet.cash)
	private val selectedWallet: LiveData<Wallet> = _selectedWallet
	
	private val _selectedFinancial = MutableLiveData(Financial.default)
	private val selectedFinancial: LiveData<Financial> = _selectedFinancial
	
	private val _availableCategory = MutableStateFlow(emptyList<Category>())
	private val availableCategory: StateFlow<List<Category>> = _availableCategory
	
	private val _selectedFinancialType = MutableStateFlow(FinancialType.INCOME)
	private val selectedFinancialType: StateFlow<FinancialType> = _selectedFinancialType
	
	private val _pieEntry = MutableStateFlow(emptyList<PieEntry>())
	private val pieEntry: StateFlow<List<PieEntry>> = _pieEntry
	
	private val _lastSelectedWalletID = MutableStateFlow(Wallet.default.id)
	private val lastSelectedWalletID: StateFlow<Int> = _lastSelectedWalletID
	
	private val availableWallets: ArrayList<Wallet> = arrayListOf()
	
	init {
		CoroutineScope(dispatcher).launch {
			appRepository.walletRepository.getAllWallet().collect { wallets ->
				_selectedWallet.postValue(
					wallets.find { it.id == lastSelectedWalletID.value } ?: Wallet.cash
				)
				
				availableWallets.clear()
				availableWallets.addAll(wallets.also { Timber.i(it.toString()) })
			}
		}
		
		CoroutineScope(Dispatchers.Main).launch {
			combine(
				appRepository.getAllFinancial(),
				lastSelectedWalletID,
				selectedFinancialType
			) { financials, walletID, financialType ->
				Triple(financials, walletID, financialType)
			}.collect { triple ->
				val incomeList = triple.first.filter {
					(it.walletID == triple.second) and (it.type == FinancialType.INCOME)
				}
				
				val expenseList = triple.first.filter {
					(it.walletID == triple.second) and (it.type == FinancialType.EXPENSE)
				}
				
				val categories = when (triple.third) {
					FinancialType.INCOME -> incomeList.getBy {
						it.category
					}.distinctBy { it.id }
					FinancialType.EXPENSE ->  expenseList.getBy {
						it.category
					}.distinctBy { it.id }
					else -> triple.first.getBy { it.category }.distinctBy { it.id }
				}
				
				_availableCategory.emit(categories)
				_selectedWallet.postValue(
					availableWallets.find { it.id == triple.second } ?: Wallet.cash
				)
				_pieEntry.emit(
					calculatePieEntry(
						when (triple.third) {
							FinancialType.INCOME -> incomeList
							FinancialType.EXPENSE -> expenseList
							else -> triple.first
						},
						categories
					)
				)
			}
		}
	}
	
	override suspend fun insertFinancial(financial: Financial) {
		appRepository.insert(financial)
	}
	
	override suspend fun updateWallet(wallet: Wallet) {
		withContext(Dispatchers.Main) {
			appRepository.walletRepository.updateWallet(
				wallet.also {
					Timber.i("wallet to update: $wallet")
				}
			)
		}
		
		_selectedWallet.postValue(Wallet.default)
		_selectedWallet.postValue(wallet)
		
	}
	
	override suspend fun deleteWallet(wallet: Wallet) {
		appRepository.walletRepository.deleteWallet(wallet)
	}
	
	override fun getAllWallet(): Flow<List<Wallet>> {
		return appRepository.walletRepository.getAllWallet()
	}
	
	override suspend fun setWalletID(id: Int) {
		_lastSelectedWalletID.emit(Wallet.default.id)
		_lastSelectedWalletID.emit(id)
	}
	
	override fun getWallet(): Flow<Wallet> {
		return selectedWallet.asFlow()
	}
	
	override suspend fun setFinancialID(id: Int) {
		_selectedFinancial.postValue(
			appRepository.get(id) ?: Financial.default
		)
	}
	
	override suspend fun setSelectedFinancialType(type: FinancialType) {
		_selectedFinancialType.emit(type)
	}
	
	override fun getFinancial(): Flow<Financial> {
		return selectedFinancial.asFlow()
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