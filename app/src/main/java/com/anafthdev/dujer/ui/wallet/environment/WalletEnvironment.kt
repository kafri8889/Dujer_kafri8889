package com.anafthdev.dujer.ui.wallet.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
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
	
	private val _incomeTransaction = MutableLiveData(emptyList<Financial>())
	private val incomeTransaction: LiveData<List<Financial>> = _incomeTransaction
	
	private val _expenseTransaction = MutableLiveData(emptyList<Financial>())
	private val expenseTransaction: LiveData<List<Financial>> = _expenseTransaction
	
	private var _lastSelectedWalletID = MutableStateFlow(Wallet.default.id)
	private var lastSelectedWalletID: StateFlow<Int> = _lastSelectedWalletID
	
	init {
		CoroutineScope(Dispatchers.Main).launch {
			appRepository.getAllFinancial()
				.combine(lastSelectedWalletID) { id, wallets ->
					id to wallets
				}
				.collect { pair ->
					_incomeTransaction.postValue(
						pair.first.filter {
							(it.walletID == pair.second) and (it.type == FinancialType.INCOME)
						}
					)
					
					_expenseTransaction.postValue(
						pair.first.filter {
							(it.walletID == pair.second) and (it.type == FinancialType.EXPENSE)
						}
					)
				}
		}
	}
	
	override suspend fun deleteWallet(wallet: Wallet) {
		appRepository.walletRepository.deleteWallet(wallet)
	}
	
	override fun getIncomeTransaction(): Flow<List<Financial>> {
		return incomeTransaction.asFlow()
	}
	
	override suspend fun getTransaction(walletID: Int) {
		_lastSelectedWalletID.emit(walletID)
	}
	
	override fun getExpenseTransaction(): Flow<List<Financial>> {
		return expenseTransaction.asFlow()
	}
	
	override fun getAllWallet(): Flow<List<Wallet>> {
		return appRepository.walletRepository.getAllWallet()
	}
	
	override suspend fun getWallet(id: Int) {
		_selectedWallet.postValue(
			if (id == Wallet.cash.id) Wallet.cash else appRepository
				.walletRepository
				.get(id)
		)
	}
	
	override fun getWallet(): Flow<Wallet> {
		return selectedWallet.asFlow()
	}
	
	override suspend fun getFinancial(id: Int) {
		_selectedFinancial.postValue(
			appRepository.get(id) ?: Financial.default
		)
	}
	
	override fun getFinancial(): Flow<Financial> {
		return selectedFinancial.asFlow()
	}
	
	
}