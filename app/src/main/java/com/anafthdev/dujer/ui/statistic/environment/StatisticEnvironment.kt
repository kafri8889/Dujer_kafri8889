package com.anafthdev.dujer.ui.statistic.environment

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
	
	override fun getWallet(walletID: Int) {
		_selectedWallet.postValue(
			appRepository.walletRepository.get(walletID) ?: Wallet.cash
		)
	}
	
	override fun getWallet(): Flow<Wallet> {
		return selectedWallet.asFlow()
	}
	
	override fun getIncomeTransaction(): Flow<List<Financial>> {
		return incomeTransaction.asFlow()
	}
	
	override fun getExpenseTransaction(): Flow<List<Financial>> {
		return expenseTransaction.asFlow()
	}
	
}