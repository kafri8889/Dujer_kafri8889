package com.anafthdev.dujer.ui.dashboard.environment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.db.model.Category
import com.anafthdev.dujer.data.db.model.Financial
import com.anafthdev.dujer.data.db.model.Wallet
import com.anafthdev.dujer.data.repository.app.IAppRepository
import com.anafthdev.dujer.foundation.di.DiName
import com.anafthdev.dujer.foundation.extension.forEachMap
import com.anafthdev.dujer.foundation.extension.getBy
import com.anafthdev.dujer.ui.financial.data.FinancialAction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class DashboardEnvironment @Inject constructor(
	@Named(DiName.DISPATCHER_IO) override val dispatcher: CoroutineDispatcher,
	private val appRepository: IAppRepository
): IDashboardEnvironment {
	
	private val _financial = MutableLiveData(Financial.default)
	private val financial: LiveData<Financial> = _financial
	
	private val _financialAction = MutableLiveData(FinancialAction.NEW)
	private val financialAction: LiveData<String> = _financialAction
	
	private val _highestExpenseCategory = MutableStateFlow(Category.default)
	private val highestExpenseCategory: StateFlow<Category> = _highestExpenseCategory
	
	private val _highestExpenseCategoryAmount = MutableStateFlow(0.0)
	private val highestExpenseCategoryAmount: StateFlow<Double> = _highestExpenseCategoryAmount
	
	init {
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
	
	override suspend fun getFinancial(): Flow<Financial> {
		return financial.asFlow()
	}
	
	override suspend fun getFinancialAction(): Flow<String> {
		return financialAction.asFlow()
	}
	
	override suspend fun getHighestExpenseCategory(): Flow<Category> {
		return highestExpenseCategory
	}
	
	override suspend fun getHighestExpenseCategoryAmount(): Flow<Double> {
		return highestExpenseCategoryAmount
	}
	
	override suspend fun setFinancialID(id: Int) {
		_financial.postValue(appRepository.get(id) ?: Financial.default)
	}
	
	override suspend fun insertWallet(wallet: Wallet) {
		appRepository.walletRepository.insertWallet(wallet)
	}
	
	override fun setFinancialAction(action: String) {
		_financialAction.postValue(action)
	}
	
}