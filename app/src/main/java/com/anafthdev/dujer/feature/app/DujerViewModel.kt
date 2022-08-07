package com.anafthdev.dujer.feature.app

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.data.UndoType
import com.anafthdev.dujer.feature.app.environment.IDujerEnvironment
import com.anafthdev.dujer.foundation.extension.merge
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import com.anafthdev.dujer.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DujerViewModel @Inject constructor(
	dujerEnvironment: IDujerEnvironment
): StatefulViewModel<DujerState, DujerEffect, DujerAction, IDujerEnvironment>(DujerState(), dujerEnvironment) {
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			environment.getCurrentCurrency().collect { currency ->
				val financialList = environment.getAllFinancial().first()
				
				changeFinancialCurrency(financialList, currency)
				
				setState {
					copy(
						currentCurrency = currency
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getDataCanBeReturned().collect { undoType ->
				setState {
					copy(
						dataCanReturned = undoType
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			environment.getAllCategory().collect { categories ->
				setState {
					copy(
						allCategory = categories.merge(Category.values)
							.sortedBy { it.name }
							.distinctBy { it.id }
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			combine(
				environment.getAllBudget(),
				environment.getAllFinancial()
			) { budgets, financials ->
				budgets to financials
			}.collect { (budgets, financials) ->
				val budgetToUpdate = arrayListOf<Budget>()
				val expenseTransaction = financials.filter { it.type == FinancialType.EXPENSE }
				val categories = expenseTransaction.map { it.category.id }
				
				categories.forEach { categoryID ->
					budgets.find { it.category.id == categoryID }?.let { budget ->
						val expenseAmount = expenseTransaction
							.filter { it.category.id == categoryID }
							.sumOf { it.amount }
						
						val remaining = budget.max - expenseAmount
						val isMaxReached = remaining <= 0.0
						
						budgetToUpdate.add(
							budget.copy(
								remaining = remaining,
								isMaxReached = isMaxReached
							)
						)
					}
				}
				
				environment.updateBudget(*budgetToUpdate.toTypedArray())
				
				setState {
					copy(
						allBudget = budgets
					)
				}
			}
		}
		
		viewModelScope.launch(environment.dispatcher) {
			combine(
				environment.getAllWallet(),
				environment.getAllFinancial()
			) { wallets, financials ->
				wallets to financials
			}.collect { (wallets, financials) ->
				listenDeletedWallet(wallets, financials)
				setState {
					copy(
						allWallet = wallets,
						allIncomeTransaction = financials.filter { it.type == FinancialType.INCOME },
						allExpenseTransaction = financials.filter { it.type == FinancialType.EXPENSE },
					)
				}
					
				val updatedWallet = arrayListOf<Wallet>()

				wallets.forEach { wallet ->
					val incomeTransaction = financials.filter {
						(it.type == FinancialType.INCOME) and (it.walletID == wallet.id)
					}.sumOf { it.amount }
					
					val expenseTransaction = financials.filter {
						(it.type == FinancialType.EXPENSE) and (it.walletID == wallet.id)
					}.sumOf { it.amount }
					
					updatedWallet.add(
						wallet.copy(
							balance = wallet.initialBalance + incomeTransaction - expenseTransaction
						)
					)
				}

				environment.updateWallet(*updatedWallet.toTypedArray())
			}
		}
	}
	
	override fun dispatch(action: DujerAction) {
		when (action) {
			is DujerAction.Undo -> {
				viewModelScope.launch(environment.dispatcher) {
					if (action.type == UndoType.Financial) {
						environment.undoFinancial()
					} else environment.undoCategory()
				}
			}
			is DujerAction.InsertWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertWallet(action.wallet)
				}
			}
			is DujerAction.DeleteFinancial -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteFinancial(*action.financials)
					withContext(Dispatchers.Main) {
						setEffect(
							DujerEffect.DeleteFinancial(action.financials[0])
						)
					}
				}
			}
			is DujerAction.DeleteCategory -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteCategory(*action.categories)
					withContext(Dispatchers.Main) {
						setEffect(
							DujerEffect.DeleteCategory(action.categories[0])
						)
					}
				}
			}
		}
	}
	
	private suspend fun changeFinancialCurrency(financialList: List<Financial>, currency: Currency) {
		environment.updateFinancial(
			*financialList
				.filter { it.currency.countryCode != currency.countryCode }
				.map { it.copy(currency = currency) }
				.toTypedArray()
		)
	}
	
	private suspend fun listenDeletedWallet(wallets: List<Wallet>, financials: List<Financial>) {
		val walletIDs = wallets.map { it.id }
		val filteredFinancials = financials.filterNot {
			it.walletID in walletIDs
		}
		
		environment.updateFinancial(
			*filteredFinancials.map {
				it.copy(walletID = Wallet.cash.id)
			}.toTypedArray()
		)
	}
	
}