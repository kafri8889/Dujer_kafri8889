package com.anafthdev.dujer.feature.app

import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Budget
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.data.model.Wallet
import com.anafthdev.dujer.feature.app.data.UndoType
import com.anafthdev.dujer.feature.app.environment.IDujerEnvironment
import com.anafthdev.dujer.foundation.common.BaseEffect
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
): StatefulViewModel<DujerState, BaseEffect, DujerAction, IDujerEnvironment>(DujerState(), dujerEnvironment) {
	
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
						allTransaction = financials,
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
					when (action.type) {
						is UndoType.Wallet -> environment.undoWallet()
						is UndoType.Category -> environment.undoCategory()
						is UndoType.Financial -> environment.undoFinancial()
					}
				}
			}
			is DujerAction.InsertWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertWallet(action.wallet)
				}
			}
			is DujerAction.SetController -> {
				viewModelScope.launch(environment.dispatcher) {
					setState {
						copy(
							controller = action.controller
						)
					}
				}
			}
			is DujerAction.DeleteFinancial -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteFinancial(*action.financials)
					action.financials.getOrNull(0)?.let {
						withContext(Dispatchers.Main) {
							setEffect(
								DujerEffect.DeleteFinancial(it)
							)
						}
					}
				}
			}
			is DujerAction.DeleteCategory -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteCategory(action.categories, action.financials)
					action.categories.getOrNull(0)?.let {
						withContext(Dispatchers.Main) {
							setEffect(
								DujerEffect.DeleteCategory(it)
							)
						}
					}
				}
			}
			is DujerAction.DeleteWallet -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.deleteWallet(*action.wallets)
					action.wallets.getOrNull(0)?.let {
						withContext(Dispatchers.Main) {
							setEffect(
								DujerEffect.DeleteWallet(it)
							)
						}
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
	
	fun sendEffect(effect: BaseEffect) {
		viewModelScope.launch {
			setEffect(effect)
		}
	}
	
}