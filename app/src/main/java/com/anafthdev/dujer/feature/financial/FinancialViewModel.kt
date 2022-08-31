package com.anafthdev.dujer.feature.financial

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.anafthdev.dujer.data.ARG_FINANCIAL_ACTION
import com.anafthdev.dujer.data.ARG_FINANCIAL_ID
import com.anafthdev.dujer.data.FinancialType
import com.anafthdev.dujer.data.model.Category
import com.anafthdev.dujer.data.model.Financial
import com.anafthdev.dujer.feature.financial.environment.IFinancialEnvironment
import com.anafthdev.dujer.foundation.common.CurrencyFormatter
import com.anafthdev.dujer.foundation.extension.deviceLocale
import com.anafthdev.dujer.foundation.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancialViewModel @Inject constructor(
	financialEnvironment: IFinancialEnvironment,
	savedStateHandle: SavedStateHandle
): StatefulViewModel<FinancialState, Unit, FinancialAction, IFinancialEnvironment>(
	FinancialState(),
	financialEnvironment
) {
	
	private val financialId = savedStateHandle.getStateFlow(ARG_FINANCIAL_ID, Financial.default.id)
	private val financialAction = savedStateHandle.getStateFlow(ARG_FINANCIAL_ACTION, com.anafthdev.dujer.feature.financial.data.FinancialAction.NEW)
	
	init {
		viewModelScope.launch(environment.dispatcher) {
			financialId.collect { id ->
				environment.getFinancial(id).collect { financial ->
					setState {
						copy(
							selectedFinancial = financial
						)
					}
				}
			}
		}
		
		viewModelScope.launch {
			financialAction.collect { action ->
				setState {
					copy(
						financialAction = action
					)
				}
			}
		}
	}
	
	override fun dispatch(action: FinancialAction) {
		when (action) {
			is FinancialAction.Update -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.updateFinancial(action.financial)
				}
			}
			is FinancialAction.Insert -> {
				viewModelScope.launch(environment.dispatcher) {
					environment.insertFinancial(action.financial)
				}
			}
			is FinancialAction.SetFinancialNew -> {
				viewModelScope.launch {
					setState {
						copy(
							financialNew = action.financial
						)
					}
				}
			}
			is FinancialAction.SetFinancialTitle -> {
				viewModelScope.launch {
					setState {
						copy(
							financialTitle = action.title
						)
					}
				}
			}
			is FinancialAction.SetFinancialAmountDouble -> {
				viewModelScope.launch {
					setState {
						copy(
							financialAmountDouble = action.amount
						)
					}
				}
			}
			is FinancialAction.SetFinancialAmount -> {
				viewModelScope.launch {
					setState {
						copy(
							financialAmount = action.value
						)
					}
				}
			}
			is FinancialAction.SetFinancialDate -> {
				viewModelScope.launch {
					setState {
						copy(
							financialDate = action.date
						)
					}
				}
			}
			is FinancialAction.SetFinancialCategoryForIncome -> {
				viewModelScope.launch {
					setState {
						copy(
							financialCategoryForIncome = action.category
						)
					}
				}
			}
			is FinancialAction.SetFinancialCategoryForExpense -> {
				viewModelScope.launch {
					setState {
						copy(
							financialCategoryForExpense = action.category
						)
					}
				}
			}
			is FinancialAction.SetFinancialType -> {
				viewModelScope.launch {
					setState {
						copy(
							financialType = action.type
						)
					}
				}
			}
			is FinancialAction.SetSelectedWallet -> {
				viewModelScope.launch {
					setState {
						copy(
							selectedWallet = action.wallet
						)
					}
				}
			}
			is FinancialAction.ResetState -> {
				viewModelScope.launch {
					setState {
						copy(
							financialNew = Financial.default,
							financialTitle = "",
							financialAmountDouble = 0.0,
							financialAmount = TextFieldValue(
								text = CurrencyFormatter.format(
									locale = deviceLocale,
									amount = 0.0,
									useSymbol = false,
									currencyCode = action.currencyCode
								)
							),
							financialDate = System.currentTimeMillis(),
							financialCategoryForIncome = Category.otherIncome,
							financialCategoryForExpense = Category.otherExpense,
							financialType = FinancialType.INCOME,
						)
					}
				}
			}
		}
	}
	
}